package com.dcmall.back.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class WebCrawlerService {
    public void scrapeWebPageWithSelenium(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            // subject-link 클래스를 가진 요소 선택
            Elements titles = doc.select(".ellipsis-with-reply-cnt");
            Elements urls = doc.select(".subject-link");

            for (Element element : titles) {
                // 요소 자체를 포함한 HTML 출력
                System.out.println("title: " + element.text());
            }

            for(Element element : urls){
                System.out.println("url: " + element.attr("href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
