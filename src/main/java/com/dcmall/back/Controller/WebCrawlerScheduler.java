package com.dcmall.back.Controller;

import com.dcmall.back.Service.WebCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class WebCrawlerScheduler {
    @Autowired
    private WebCrawlerService webCrawlerService;

    @Scheduled(fixedRate = 600000) // 10분(600,000밀리초)마다 실행
    public void scheduleCrawling() {
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

            System.out.println("All scheduled crawling completed successfully");
        } catch (Exception e) {
            System.out.println("Scheduled crawling error: " + e.getMessage());
        }
    }
}
