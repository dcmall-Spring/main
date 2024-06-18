package com.dcmall.back.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WebCrawlerService {
    public void scrapeWebPageWithSelenium(String url) {
        try {
            // URL로부터 Document 객체를 가져옴
            Document doc = Jsoup.connect(url).get();

            // HTML 페이지의 제목(title) 가져오기
            String title = doc.title();
            System.out.println("Title: " + title);

            // 특정 요소 추출 (예: 모든 링크(anchor) 태그)
            Elements links = doc.select(".ellipsis-with-reply-cnt ");
            Elements titles= doc.select(".subject-link");

            for (Element link : links) {
                // 링크의 URL과 텍스트 출력
                System.out.println("Text: " + link.html());
            }
            for(Element title2 : titles){
                System.out.println("Title: " + title2.attr("href"));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
        
}
