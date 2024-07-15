package com.dcmall.back.Controller;

import com.dcmall.back.Service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

/*
<a href="/bbs/qb_saleinfo/views/1609772" class="subject-link " style="display: block;">
퀘이사 존은 최신 순으로 나열했을 때, views 뒤에 숫자가 커지는 경향이 있으니 저장해뒀다가 미만은 안 받아오게 막자
*/
@RestController
public class WebController {
    @Autowired
    public WebCrawlerService webCrawlerService;

    @GetMapping("/scrape")
    public String scrape() {

        //String url = "https://quasarzone.com/bbs/qb_saleinfo";
        try {
            //webCrawlerService.scrapeQuasarzone(url);
            //webCrawlerService.scrapefmkorea("https://www.fmkorea.com/hotdeal");
            //webCrawlerService.scrapeRuliWeb("https://m.ruliweb.com/market/board/1020");
            webCrawlerService.scrapeArcalive("https://arca.live/b/hotdeal/");
            return "Complete";
        } catch (Exception e) {
            System.out.println("퀘이사 크롤링 컨트롤 오류");
            return "Error: " + e.getMessage();
        }
    }
}
