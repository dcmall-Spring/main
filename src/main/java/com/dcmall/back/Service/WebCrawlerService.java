package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebCrawlerService {
    @Autowired
    ProductInfoDAO dao;

    public void scrapeWebPageWithSelenium(String url) {
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listTitle = new ArrayList<>();
        String product = this.dao.selectProduct(1);

        int postNumber = 0;
        if(product != null){
            postNumber = Integer.parseInt(product.substring(45));
        }

        try {
            Document doc = Jsoup.connect(url).get();

            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".ellipsis-with-reply-cnt");
            Elements urls = doc.select(".subject-link");

            for (Element element : titles) {
                // 요소 자체를 포함한 HTML 출력
                listTitle.add(element.text());
            }

            for(Element element : urls){
                if(element.childrenSize() > 0){
                    listUrl.add("https://quasarzone.com" + element.attr("href"));
                }
            }
            // 출력할 요소를 list에 저장 후 db에 저장.
            for (int i = 0; i < listTitle.size(); i++) {
                if(postNumber < Integer.parseInt(listUrl.get(i).substring(45)) && product != null){
                    dao.insertProduct("1", listTitle.get(i), listUrl.get(i));
                }
            }

            sendTitlesToClient(listTitle);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void sendTitlesToClient(List<String> titles) {
        String url = "http://localhost:3000/api/receive-titles";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> entity = new HttpEntity<>(titles, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("데이터 전송 성공: " + response.getBody());
            } else {
                System.out.println("데이터 전송 실패: " + response.getStatusCode() + ", " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sendTitlesToClient 예외 발생: " + e.getMessage());
        }
    }
}
