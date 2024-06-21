package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
import com.dcmall.back.model.ProductInfoDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebCrawlerService {
    @Autowired
    ProductInfoDAO dao;

    public void scrapeWebPageWithSelenium(String url) {
        System.out.println("url");
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
        String product = this.dao.selectProduct(1);

        /*
            이럴거면 URL을 게시글 post 숫자만 파악해서 저장하는게 괜찮을거 같은데.. 고민좀 해봐야 될듯.
         */
        int postNumber = 0;
        if(product != null){
            postNumber = Integer.parseInt(product.substring(45));
        } 
        
        System.out.println(postNumber);

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

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
