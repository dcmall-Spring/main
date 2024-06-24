package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class WebCrawlerService {

    @Autowired
    ProductInfoDAO dao;
    //일반 Queue와는 달리 멀티스레드 환경을 염두해 설계된 Queue
    private BlockingQueue<List<String>> blockQue = new LinkedBlockingQueue<>();

    public void scrapeWebPageWithSelenium(String url) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        ArrayList<String> listCost = new ArrayList<>();
        String product = this.dao.selectProduct(1);

        int postNumber = 0;

        if(product != null){
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

            for(int i = 0; i < titles.size(); i ++){
                if(Integer.parseInt(urls.get(i).attr("href").substring(23)) > postNumber){
                    listTitle.add(titles.get(i).text());
                    listUrl.add(urls.get(i).attr("href").substring(23));
                    listCost.add(costs.get(i).text());
                }
            }

            // 출력할 요소를 list에 저장 후 db에 저장.
            for (int i = 0; i < listTitle.size(); i++) {
                dao.insertProduct("1", listTitle.get(i), listCost.get(i), listUrl.get(i));
            }

            blockQue.add(new ArrayList<>(listTitle));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @Scheduled(fixedRate = 10000) //10초
    public void sendTitlesFromQueue(){
        List<String> titles = blockQue.poll();
        if(titles != null){
            boolean success = sendTitlesToClient(titles);
            if(!success){
                //전송 실패 시 다시 큐에 추가
                blockQue.add(titles);
            }
        }
    }
    private boolean sendTitlesToClient(List<String> titles) {  //여기 titles이 비어있네
        String url = "http://localhost:3000/api/receive-titles";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<String>> entity = new HttpEntity<>(titles, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {   //성공 시 next.js가 보낸 message : Success를 띄운다.
                System.out.println("데이터 전송 성공: " + response.getBody());
                return true;
            } else {
                System.out.println("데이터 전송 실패: " + response.getStatusCode() + ", " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sendTitlesToClient 예외 발생: " + e.getMessage());
            return false;
        }
    }
}
