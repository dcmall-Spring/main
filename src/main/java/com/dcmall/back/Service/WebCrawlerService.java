package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
import com.dcmall.back.model.embedDAO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

        public ruilwebCost(BigDecimal cost, int square) {
            this.cost = cost;
            this.square = square;
        }

        public BigDecimal getCost() {
            return cost;
        }

        public int getSquare() {
            return square;
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
            Elements titles = doc.select(".ellipsis-with-reply-cnt");
            Elements urls = doc.select(".subject-link");
            Elements costs = doc.select(".text-orange");
            for (int i = titles.size() - 1; i >= 0; i--) {
                if (Integer.parseInt(urls.get(i).attr("href").substring(23)) > postNumber) {
                    String cost = costs.get(i).text().substring(1).split("\\(")[0].trim();
                    String[] title = titles.get(i).text().split("]");
                    deleteCost(title[1], cost);
                    listTitle.add(titles.get(i).text().split("]")[1]);
                    listUrl.add(urls.get(i).attr("href").substring(23));
                    listCost.add(costs.get(i).text());
                }
            }


            for(String s : listTitle){
                System.out.println("title " + s);
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
                    ArrayList<ruilwebCost> ruilwebResult = getCost(titles.get(i).text());
                    if (!ruilwebResult.isEmpty()) {
                        ruilwebCost ruil = ruilwebResult.get(0);
                        int square = ruil.getSquare();
                        BigDecimal cost = ruil.getCost();
                        BigDecimal total = cost.multiply(BigDecimal.TEN.pow(square)); // BigDecimal의 pow 메서드 사용

                        String price = total.toString();
                        String AmericanPrice = NumberFormat.getInstance().format(total);

                        try{
                            String[] normal = titles.get(i).text().split(price);
                            String[] American = titles.get(i).text().split(AmericanPrice);

                            if(American.length >= 2){
                                if(realPrice(American) == 0){
                                    price = "0";
                                }else if(realPrice(American) == 1){
                                    if(price.equals("0"))
                                        price = "0";
                                    else
                                        price = ("₩ "+price+" (KRW)");
                                }
                                else{
                                    if(price.equals("0"))
                                        price = "0";
                                    else
                                        price = ("$ " + price + " (USD)");
                                }
                            }else if(normal.length >= 2){
                                if(realPrice(normal) == 0){
                                    price = "0";
                                }else if(realPrice(normal) == 1){
                                    if(price.equals("0"))
                                        price = "0";
                                    else
                                        price = ("₩ "+price+" (KRW)");
                                }else{
                                    if(price.equals("0"))
                                        price = "0";
                                    else
                                        price = ("$ " + price + " (USD)");
                                }
                            }else if(square > 0){
                                price = total.setScale(0, RoundingMode.DOWN).toString();
                                price = ("₩ "+price+" (KRW)");
                            }else{
                                price = "0";
                            }
                        }catch (Exception e){
                            price = "0";
                        }


                        listCost.add(price);
                        listTitle.add(titles.get(i).text());
                        postNumber = Integer.parseInt(censored[censored.length-1]);
                        listUrl.add(censored[censored.length-1]);
                    }else{
                        listCost.add("0");
                        listTitle.add(titles.get(i).text());
                        postNumber = Integer.parseInt(censored[censored.length-1]);
                        listUrl.add(censored[censored.length-1]);
                    }
                }
            }
            inputDB("3", listTitle, listCost, listUrl);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("루리웹 오류: " + e.getMessage());
        }
    }

    private ArrayList<ruilwebCost> getCost(String title) {
        System.out.println("타이틀: "+title);
        int square = 0;
        StringBuilder sb = new StringBuilder();
        boolean foundNumber = false;
        boolean foundUnit = false;

        for (int i = title.length() - 1; i >= 0; i--) {
            char c = title.charAt(i);
            if (Character.isDigit(c)) {
                sb.insert(0, c);
                foundNumber = true;
            } else if (c == ',' && foundNumber) {
                continue;  // 숫자 사이의 쉼표는 무시
            } else if (!foundNumber) {
                int index = checkUnit(c);
                if (index > -1) {
                    square += index;
                    foundUnit = true;
                } else if (c == '.' && foundUnit) {
                    sb.insert(0, c);
                }
            } else if (foundNumber) {
                if (!Character.isDigit(c)) {
                    if(c == '.'){
                        sb.insert(0, c);
                    }else{
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
                result.add(new ruilwebCost(value, square));
            } catch (NumberFormatException e) {
                BigDecimal value = new BigDecimal("0");
                result.add(new ruilwebCost(value, 0));
            }
        } else {
            BigDecimal value = new BigDecimal("0");
            result.add(new ruilwebCost(value, 0));
        }

        return result;
    }

    public int checkUnit(char c){
        char[] unit = {'일', '십', '백', '천', '만'};
        int index = -1;

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
        }

        return 0;
    }

    private void inputDB(String siteNumber, ArrayList<String> listTitle, ArrayList<String> listCost, ArrayList<String> listUrl) throws IOException {
        for (int i = 0; i < listTitle.size(); i++) {
            String sTitle = listTitle.get(i);
            if(eDao.isExist(sTitle) == null)
            {
                var result = embeddingService.getEmbedding(sTitle);

                eDao.insertEmbed(listTitle.get(i), result);
            }

            dao.insertProduct(siteNumber, listTitle.get(i), listCost.get(i), listUrl.get(i));
        }
    }

    public ArrayList<String> deleteCost(String title, String cost) {

        ArrayList<String> result = new ArrayList<>();
        
        int start = -1;

        int costCheck = 0;
        
        for(int i = 1 ; i < title.length() ; i++){
            if(title.charAt(i) == '[' || title.charAt(i) == '('){
                start = i;
            } else if (title.charAt(i) == ']' || title.charAt(i) == ')' && start != -1) {
                System.out.println("start count : " + start);
                System.out.println("괄호내용 : " + title.substring(start + 1, i));
                String costEqual = title.substring(start + 1, i).trim();
                System.out.println("costEqual : " + costEqual);
                for(int j = 0; j < costEqual.length(); j++){
                    if(costEqual.charAt(j) == cost.charAt(costCheck)){
                        costCheck ++;
                        if(costCheck >= cost.length()){
                            break;
                        }
                    } else {
                        costCheck = 0;
                    }
                }
                System.out.println("length : " + cost.length());
                if(costCheck == cost.length()){
                    title = title.substring(1, start);
                    System.out.println("answer : " + title);
                }
                start = -1;
            }
        }

        System.out.println("stop");

        return result;
    }
}
