package com.dcmall.back.Service;

import com.dcmall.back.model.ProductInfoDAO;
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

    public void scrapeWebPageWithSelenium(String url) {
        ArrayList<String> listTitle = new ArrayList<>();
        ArrayList<String> listUrl = new ArrayList<>();
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
                    if(urls.get(i).childrenSize() > 0){
                        listUrl.add(urls.get(i).attr("href").substring(23));
                    }
                }
            }

            // 출력할 요소를 list에 저장 후 db에 저장.
            for (int i = 0; i < listTitle.size(); i++) {
                dao.insertProduct("1", listTitle.get(i), listUrl.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
