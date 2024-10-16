package com.dcmall.back.Controller;

import com.dcmall.back.Service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

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
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    webCrawlerService.scrapeQuasarzone("https://quasarzone.com/bbs/qb_saleinfo");
                    System.out.println("Quasarzone crawling completed");
                } catch (Exception e) {
                    System.out.println("Quasarzone crawling error: " + e.getMessage());
                }
            /*}).thenRun(() -> {
                try {
                    webCrawlerService.scrapefmkorea("https://www.fmkorea.com/hotdeal");
                    System.out.println("FMKorea crawling completed");
                } catch (Exception e) {
                    System.out.println("FMKorea crawling error: " + e.getMessage());
                }*/
            }).thenRun(() -> {
                try {
                    webCrawlerService.scrapeRuliWeb("https://m.ruliweb.com/market/board/1020");
                    System.out.println("RuliWeb crawling completed");
                } catch (Exception e) {
                    System.out.println("RuliWeb crawling error: " + e.getMessage());
                }
            }).thenRun(() -> {
                try {
                    webCrawlerService.scrapeArcalive("https://arca.live/b/hotdeal/");
                    System.out.println("Arcalive crawling completed");
                } catch (Exception e) {
                    System.out.println("Arcalive crawling error: " + e.getMessage());
                }
            }).join();

            return "All scheduled crawling completed successfully";
        } catch (Exception e) {
            return "Scheduled crawling error: " + e.getMessage();
        }
    }
}
