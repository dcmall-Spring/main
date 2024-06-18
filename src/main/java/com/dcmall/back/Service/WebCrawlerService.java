package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
public class WebCrawlerService {
    ProductInfoDAO dao;

    public void scrapeWebPageWithSelenium(String url) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
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
                    listUrl.add("https://quasarzone.com/" + element.attr("href"));
                }
            }
            for (int i = 0; i < listTitle.size(); i++) {
                int b = dao.insertProduct("1", listTitle.get(i), listUrl.get(i));
                System.out.println(b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
