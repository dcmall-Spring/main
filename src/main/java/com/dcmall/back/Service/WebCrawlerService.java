package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
import com.dcmall.back.model.embedDAO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Service
public class WebCrawlerService {

    @Autowired
    ProductInfoDAO dao;
    @Autowired
    EmbeddingService embeddingService;
    @Autowired
    embedDAO eDao;

    class ruilwebCost {
        BigDecimal cost;
        int square;
        int index;

        public ruilwebCost(BigDecimal cost, int square, int index) {
            this.cost = cost;
            this.square = square;
            this.index = index;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public int getSquare() {
            return square;
        }

        public int getIndex(){
            return index;
        }
    }

    public void scrapeQuasarzone(String url) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        String product = this.dao.selectProduct(1);

        int postNumber = 0;

        if (product != null) {
            postNumber = Integer.parseInt(product);
        }

        try {
            Document doc = Jsoup.connect(url).get();
            /*
                title 을 [ ( 등 나올떄 글자를 한글자 씩 잘라 cost 가격과 일치하는 것이 있는지 확인 있을시 [ ( 괄호 안의 내용 제거 함수로 만들기
             */
            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".subject-link .ellipsis-with-reply-cnt, .subject-link .fa.fa-lock");
            Elements urls = doc.select(".subject-link");
            Elements costs = doc.select(".text-orange");
            for (int i = titles.size() - 1; i >= 0; i--) {
                if (Integer.parseInt(urls.get(i + 3).attr("href").substring(23)) > postNumber && !titles.get(i).hasClass("fa fa-lock")) {
                    String cost = costs.get(i).text().substring(1).split("\\(")[0].trim();
                    String title = titles.get(i).text().replaceFirst("^\\[.*?\\]\\s*", "");
                    String titleQuasa = deleteCost(title, cost);
                    listTitle.add(titleQuasa);
                    listUrl.add(urls.get(i + 3).attr("href").substring(23));
                    listCost.add(costs.get(i).text());
                }
            }



            //inputDB("1", listTitle, listCost, listUrl);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void scrapefmkorea(String url) throws IOException {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        String product = this.dao.selectProduct(2);

        int postNumber = 0;

        if (product != null) {
            postNumber = Integer.parseInt(product);
        }
        /*
            fmkorea 는 자체적으로 크롤링을 방지함 그렇기 때문에 테스트 진행을 위해 jsoup 을 변조하여 평범한 사용자처럼 위장함
            그런데 이렇게 하는게 올바른 방법인지 모르겠음.
         */
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 Edg/91.0.864.59")
                    .execute();

            System.out.println(response);

            Document doc = Jsoup.connect(url).get();

            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".hotdeal_var8");
            Elements costs = doc.select(".hotdeal_info span:contains(가격)");
            Elements delTitle = doc.select(".hotdeal_var8Y[href]");
            System.out.println("titles: " + titles.get(1).text());
            System.out.println("Url: " + titles.get(1).text());
            System.out.println("titles: " + titles.get(1).attr("href"));
            System.out.println("delete: " + delTitle.get(0));

            Collections.reverse(titles);
            Collections.reverse(costs);
            System.out.println("costs: " + delTitle.get(0));
            System.out.println("aa");


            System.out.println("titles number : " + titles.size());
            System.out.println("costs number : " + costs.size());

            if(titles.size() == costs.size()) {
                for (int i = 0; i < titles.size(); i++) {
                    if (Long.parseLong(titles.get(i).attr("href").substring(1)) > postNumber) {
                        listTitle.add(titles.get(i).text());
                        listUrl.add(titles.get(i).attr("href").substring(1));
                        listCost.add(costs.get(i).text());
                    }
                }
            } else {
                for (int i = 0; i < titles.size(); i++) {
                    System.out.println(costs.get(i).selectFirst("a").attr("href")+ " " + titles.get(i).attr("href"));
                    if (Long.parseLong(titles.get(i).attr("href").substring(1)) > postNumber &&
                            costs.get(i).attr("href").equals(titles.get(i).attr("href"))) {
                        listTitle.add(titles.get(i).text());
                        listUrl.add(titles.get(i).attr("href").substring(1));
                        listCost.add(costs.get(i).text());
                    }
                }
            }
        System.out.println("aaa");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void scrapeRuliWeb(String url) throws IOException {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        String product = this.dao.selectProduct(3);

        int postNumber = 0;

        if (product != null) {
            postNumber = Integer.parseInt(product);
        }

        try {
            Document doc = Jsoup.connect(url).get();
            Elements titles = doc.select("a.subject_link.deco[href*='1020']:not(:has(strong))");

            for (int i = titles.size() - 1; i >= 0; i--) {
                String[] link = titles.get(i).attr("href").split("1020");
                String[] censored = link[1].substring(0, link[1].length()-1).split("/");

                if(Integer.parseInt(censored[censored.length-1]) > postNumber){
                    boolean checkCost = false;
                    int square = 0;
                    BigDecimal cost = new BigDecimal(0);
                    BigDecimal total = new BigDecimal(0);
                    String price = "";
                    ArrayList<ruilwebCost> ruilwebResult = new ArrayList<>();
                    ruilwebResult.add(new ruilwebCost(total, 0, Integer.MAX_VALUE));

                    while(!checkCost){
                        ruilwebResult = getCost(titles.get(i).text(), (ruilwebResult.get(0).getIndex() == Integer.MAX_VALUE) ? titles.get(i).text().length() : ruilwebResult.get(0).getIndex() );
                        //System.out.println("검사: "+ruilwebResult.get(0).getCost()+" // "+ruilwebResult.get(0).getSquare());
                        if (ruilwebResult.get(0).getCost().intValue() != -1) {
                            ruilwebCost ruil = ruilwebResult.get(0);
                            square = ruil.getSquare();
                            cost = ruil.getCost();
                            total = cost.multiply(BigDecimal.TEN.pow(square)); // BigDecimal의 pow 메서드 사용

                            price = total.toString();
                            String AmericanPrice = NumberFormat.getInstance().format(total);

                            try{
                                String[] normal = titles.get(i).text().split(price);
                                String[] American = titles.get(i).text().split(AmericanPrice);

                                if(American.length >= 2){
                                    if(realPrice(American) == 0 && price.length() >= 4){
                                        //price = "0";
                                        checkCost = true;
                                        price = ("₩ "+price+" (KRW)");
                                    }else if(realPrice(American) == 1){
                                        if(price.equals("0")){
                                            checkCost = true;
                                            price = "0";
                                        }
                                        else{
                                            checkCost = true;
                                            price = ("₩ "+price+" (KRW)");
                                        }
                                    }
                                    else if(realPrice(American) == 2){
                                        if(price.equals("0")){
                                            checkCost = true;
                                            price = "0";
                                        }
                                        else{
                                            checkCost = true;
                                            price = ("$ " + price + " (USD)");
                                        }
                                    } else if(realPrice(American) == -1){
                                        price = "0";
                                    }
                                }else if(normal.length >= 2){
                                    if(realPrice(normal) == 0){
                                        price = "0";
                                    }else if(realPrice(normal) == 1){
                                        if(price.equals("0")) {
                                            checkCost = true;
                                            price = "0";
                                        }
                                        else{
                                            checkCost = true;
                                            price = ("₩ "+price+" (KRW)");
                                        }
                                    }else if(realPrice(normal) == 2){
                                        if(price.equals("0")){
                                            checkCost = true;
                                            price = "0";
                                        }
                                        else{
                                            checkCost = true;
                                            price = ("$ " + price + " (USD)");
                                        }
                                    } else if(realPrice(normal) == -1){
                                        price = "0";
                                    }
                                }else if(square > 0){
                                    price = total.setScale(0, RoundingMode.DOWN).toString();
                                    price = ("₩ "+price+" (KRW)");
                                    checkCost = true;
                                }else{
                                    price = "0";
                                    checkCost = true;
                                }
                            }catch (Exception e){
                                price = "0";
                                checkCost = true;
                            }
                        }else{
                            price = "0";
                            checkCost = true;
                        }
                    }
                    String costCutTitle = removeHead(titles.get(i).text());

                    if(costCutTitle.contains(price)){
                        costCutTitle = deleteCost(costCutTitle, price);
                    }else if(costCutTitle.contains(NumberFormat.getInstance().format(total))){
                        costCutTitle = deleteCost(costCutTitle, NumberFormat.getInstance().format(total));
                    }

                    listCost.add(price);
                    listTitle.add(costCutTitle);
                    postNumber = Integer.parseInt(censored[censored.length-1]);
                    listUrl.add(censored[censored.length-1]);
                }
            }
            inputDB("3", listTitle, listCost, listUrl);
            System.out.println("루리웹 크롤링 성공!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("루리웹 오류: " + e.getMessage());
        }
    }

    public void scrapeArcalive(String url) throws IOException {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        String product = this.dao.selectProduct(4);

        int postNumber = 0;

        if (product != null) {
            postNumber = Integer.parseInt(product);
        }

        try{
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select("div.vrow-inner:not(:has(div.vrow-top.deal.deal-close))");
            for(int i = row.size()-1 ; i >= 0 ; i--){
                try{
                    String post = row.get(i).select("a.title.hybrid-title").attr("href");
                    String[] postSplit = post.split("/");
                    String[] realPost = postSplit[postSplit.length-1].split("\\?");
                    if(Integer.parseInt(realPost[0]) > postNumber){
                        try {
                            String title =  Objects.requireNonNull(row.get(i).select("a.title.hybrid-title").first()).ownText().trim();
                            String price = row.get(i).select("span.deal-price").text();

                            StringBuilder sb = new StringBuilder(price);
                            if(price.contains("원")){
                                sb.setLength(sb.length() - 1);
                                sb.insert(0,"₩ ");
                                sb.insert(sb.length(), " (KRW)");
                            }else if(price.contains("$")){
                                sb.insert(0,"$ ");
                                sb.insert(sb.length(), " (USD)");
                            }

                            listTitle.add(title);
                            listCost.add(sb.toString());
                            listUrl.add(realPost[0]);
                        } catch(Exception e) {
                            System.out.println("error : " + e);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("안되네~: "+e.getMessage());
                }
            }
            inputDB("4", listTitle, listCost, listUrl);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("아카라이브 스크랩 오류: "+e.getMessage());
        }
    }

    private ArrayList<ruilwebCost> getCost(String title, int index) {
        int square = 0;
        StringBuilder sb = new StringBuilder();
        boolean foundNumber = false;
        boolean foundUnit = false;
        int numberidx = -1;

        for (int i = index - 1; i >= 0; i--) {
            char c = title.charAt(i);
            if (Character.isDigit(c)) {
                sb.insert(0, c);
                foundNumber = true;
            } else if (c == ',' && foundNumber) {
                continue;  // 숫자 사이의 쉼표는 무시
            } else if (!foundNumber) {
                int unitIndex = checkUnit(c);
                if (index > -1) {
                    square += unitIndex;
                    foundUnit = true;
                } else if (c == '.' && foundUnit) {
                    sb.insert(0, c);
                }
            } else if (foundNumber) {
                if (!Character.isDigit(c)) {
                    if(c == '.'){
                        sb.insert(0, c);
                    }else{
                        numberidx = i;
                        break;
                    }
                }
            }
        }

        ArrayList<ruilwebCost> result = new ArrayList<>();
        String numberStr = sb.toString().trim();

        if (!numberStr.isEmpty() && !numberStr.equals(".")) {
            try {
                BigDecimal value = new BigDecimal(numberStr);
                result.add(new ruilwebCost(value, square, numberidx));
            } catch (NumberFormatException e) {
                BigDecimal value = new BigDecimal("0");
                result.add(new ruilwebCost(value, 0, numberidx));
            }
        } else {
            BigDecimal value = new BigDecimal("-1");
            result.add(new ruilwebCost(value, 0, numberidx));
        }

        return result;
    }

    public int checkUnit(char c){
        char[] unit = {'일', '십', '백', '천', '만'};
        int index = 0;

        for(int i = 0 ; i < unit.length ; i++){
            if(unit[i] == c) index = i;
        }
        return index;
    }

    public int realPrice(String[] arr){
        if(arr[arr.length-2].charAt(arr[arr.length-2].length()-1) == '₩' || (arr[arr.length-1].charAt(0) == '원')){    //원화 1
            return 1;
        }else if(arr[arr.length-2].charAt(arr[arr.length-2].length()-1) == '$' ||(arr[arr.length-1].charAt(0) == '달' && arr[arr.length-1].charAt(1) == '러')){ //달러 2
            return 2;
        }else if(isUnit(arr[arr.length-1].charAt(0))) return -1;

        return 0;
    }
    private boolean isUnit(char ch) {
        return (ch == 'm' || ch == 'M') ||
                (ch == 'g' || ch == 'G') ||
                (ch == 'k' || ch == 'K') ||
                (ch == 'l' || ch == 'L') ||
                (ch == 'c' || ch == 'C') ||
                (ch == 'n' || ch == 'N') ||
                (ch == 'a' || ch == 'A') ||
                (ch == 'v' || ch == 'V') ||
                (ch == 'h' || ch == 'H') ||
                (ch == 's' || ch == 'S') ||
                (ch == 'w' || ch == 'W') ||
                (ch == 'j' || ch == 'J') ||
                (ch == 'p' || ch == 'P') ||
                (ch == 'f' || ch == 'F') ||
                (ch == 'b' || ch == 'B') ||
                (ch == 't' || ch == 'T') ||
                (ch == '%');
    }

    private void inputDB(String siteNumber, ArrayList<String> listTitle, ArrayList<String> listCost, ArrayList<String> listUrl) throws IOException {
        for (int i = 0; i < listTitle.size(); i++) {
            String sTitle = listTitle.get(i);
            if (eDao.isExist(sTitle) == null) {
                var result = embeddingService.getEmbedding(sTitle);

                eDao.insertEmbed(listTitle.get(i), result);
            }

            dao.insertProduct(siteNumber, listTitle.get(i), listCost.get(i), listUrl.get(i));
        }
    }

    public String deleteCost(String title, String cost) {
        if(!title.contains("(") && !title.contains("[")){
            return title;
        }

        String deleteCommas = cost.replaceAll(",", "");
        double number = Double.parseDouble(deleteCommas);
        String formattedNumber;
        if(number == Math.floor(number)){
            formattedNumber = String.format("%,d", (long)number);
        } else{
            formattedNumber = String.format("%,.2f", number);
        }

        int start = -1;

        int costCheck = 0;

        int end = 0;

        String endwith =  title.substring(title.length() - 3);

        for(int i = 1 ; i < title.length() ; i++){
            if(title.charAt(i) == '[' || title.charAt(i) == '('){
                start = i;
            } else if (title.charAt(i) == ']' || title.charAt(i) == ')' || endwith.equals("...") && start != -1) {
                String costEqual;
                if(title.contains("...")){
                    int count = title.indexOf("...");
                    costEqual = title.substring(start + 1, count).trim();
                } else {
                    costEqual = title.substring(start + 1, i).trim();
                    end = i + 1;
                }

                for(int j = 0; j < costEqual.length(); j++){
                    boolean deleteCheck = true;
                    if(costCheck >= formattedNumber.length()){
                        break;
                    }
                    System.out.println("현재값 : " + title);
                    if(deleteCommas.length() > costCheck){
                        if(costEqual.charAt(j) == deleteCommas.charAt(costCheck)){
                            costCheck++;
                            deleteCheck = false;
                        }
                    }

                    if(deleteCheck){
                        if(costEqual.charAt(j) == formattedNumber.charAt(costCheck)){
                            costCheck++;
                        } else if (costCheck < formattedNumber.length() -1) {
                            costCheck = 0;
                        }
                    }
                }
                if(costCheck == formattedNumber.length() || costCheck == deleteCommas.length()){
                    String firstTitle = title.substring(0, start);
                    firstTitle += title.substring(end, title.length());
                    title = firstTitle;
                }
                start = -1;
            }
        }
        System.out.println("함수안 : "  + title);
        return title;
    }

    private String removeHead(String title) {
        if (title.isEmpty()) {
            return title;
        }

        if (title.charAt(0) == '[') {
            int firstCloseBracket = title.indexOf(']');
            // 닫는 대괄호가 있는지 확인
            if (firstCloseBracket != -1) {
                // 첫 번째 닫는 대괄호 이후의 문자열을 가져오고 앞뒤 공백을 제거
                String remainingTitle = title.substring(firstCloseBracket + 1).trim();

                // 남은 문자열이 비어있지 않고 '['로 시작하면 그대로 반환
                if (!remainingTitle.isEmpty() && remainingTitle.charAt(0) == '[') {
                    return remainingTitle;
                }

                // 그 외의 경우, 남은 문자열을 반환
                return remainingTitle;
            } else {
                // 닫는 대괄호가 없는 경우, 원래 문자열을 반환
                return title;
            }
        } else {
            return title;
        }
    }

}
