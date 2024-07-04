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
import java.text.NumberFormat;
import java.util.ArrayList;
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

            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".ellipsis-with-reply-cnt");
            Elements urls = doc.select(".subject-link");
            Elements costs = doc.select(".text-orange");

            Collections.reverse(urls);
            Collections.reverse(titles);
            Collections.reverse(costs);

            for (int i = 0; i < titles.size(); i++) {
                if (Integer.parseInt(urls.get(i).attr("href").substring(23)) > postNumber) {
                    listTitle.add(titles.get(i).text());
                    listUrl.add(urls.get(i).attr("href").substring(23));
                    listCost.add(costs.get(i).text());
                }
            }

            // 출력할 요소를 list에 저장 후 db에 저장.
            //eDao가 null이 아닌데 eDao.insertEmbed가 null임 뭔 상황
            //select는 되는 엿같은 상황
            for (int i = 0; i < listTitle.size(); i++) {    //tmp의 타입은 ArrayList
                String sTitle = listTitle.get(i);
                var result = embeddingService.getEmbedding(sTitle);

                eDao.insertEmbed(listTitle.get(i), result);

                dao.insertProduct("1", listTitle.get(i), listCost.get(i), listUrl.get(i));
            }

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
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15")
                    .execute();

            System.out.println(response);

            Document doc = Jsoup.connect(url).get();

            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".hotdeal_var8");
            Elements costs = doc.select(".hotdeal_info");
            System.out.println("titles: " + titles.get(1).text());
            System.out.println("Url: " + titles.get(1).text());
            System.out.println("titles: " + titles.get(1).attr("href"));
            System.out.println("titles: " + costs.get(1).select("span:contains(가격)"));


            Collections.reverse(titles);
            Collections.reverse(costs);

            System.out.println("titles number : " + titles.size());
            System.out.println("costs number : " + costs.size());

            for (int i = 0; i < titles.size(); i++) {
                if (Integer.parseInt(titles.get(i).attr("href").substring(1)) > postNumber) {
                    listTitle.add(titles.get(i).text());
                    listUrl.add(titles.get(i).attr("href").substring(1));
                    listCost.add(costs.get(i).text());
                }
            }

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
            Elements titles = doc.select("a.subject_link.deco");
            for (int i = titles.size() - 1; i > 4; i--) {
                ArrayList<ruilwebCost> ruilwebResult = getCost(titles.get(i).text());
                if (!ruilwebResult.isEmpty()) {
                    ruilwebCost ruil = ruilwebResult.get(0);
                    int square = ruil.getSquare();
                    BigDecimal cost = ruil.getCost();
                    BigDecimal total = cost.multiply(new BigDecimal(Math.pow(10, square)));

                    String price = Integer.toString(total.intValue());
                    String AmericanPrice = NumberFormat.getInstance().format(total.intValue());

                    String[] normal = titles.get(i).text().split(price);
                    String[] American = titles.get(i).text().split(AmericanPrice);

                    if(American.length == 2){
                        if(realPrice(American)){
                            System.out.println("진짜 가격으로 임명한다: "+price);
                        }
                        else price = "0";
                    }else if(normal.length == 2){
                        if(realPrice(normal)){
                            System.out.println("진짜 가격으로 임명한다: "+price);
                        }
                        else price = "0";
                    }
                    else{
                        //둘 다 없으면 그냥 가격이라고 인정해
                        if(square >= 1){
                            System.out.println("진짜 가격으로 임명한다: "+price);
                        }

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("루리웹 오류: " + e.getMessage());
        }
    }

    private ArrayList<ruilwebCost> getCost(String title) {
        System.out.println("타이틀: " + title);
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
                System.out.println("숫자 변환 중 오류 발생: " + numberStr);
            }
        } else {
            System.out.println("유효한 숫자가 추출되지 않았습니다.");
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

    public boolean realPrice(String[] arr){
        if(arr[0].charAt(arr[0].length()-1) == '$' || arr[0].charAt(arr[0].length()-1) == '₩') {
            return true;
        }
        else if((arr[1].contains("원") && arr[1].charAt(0) == '원') || (arr[1].contains("달러") && (arr[1].charAt(0) == '달' && arr[1].charAt(1) == '러'))){
            return true;
        }

        return false;
    }
}
