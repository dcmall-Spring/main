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
}
