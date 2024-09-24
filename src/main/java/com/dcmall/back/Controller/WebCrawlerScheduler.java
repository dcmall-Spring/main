package com.dcmall.back.Controller;

import com.dcmall.back.Service.NotificationService;
import com.dcmall.back.Service.WebCrawlerService;
import com.dcmall.back.model.embedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class WebCrawlerScheduler {
    @Autowired
    private WebCrawlerService webCrawlerService;
    @Autowired
    private embedDAO embedDAO;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(fixedRate = 600000) // 10분(600,000밀리초)마다 실행
    public void scheduleCrawling() {
        Object result = embedDAO.selectEmbedNum();
        int num;
        if (result == null) {
            num = 0;
            System.out.println("null");
        }
        else if (result instanceof Integer) {
            num = (Integer) result;
        } else {
            num = 0;
        }
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    webCrawlerService.scrapeQuasarzone("https://quasarzone.com/bbs/qb_saleinfo");
                    System.out.println("Quasarzone crawling completed");
                } catch (Exception e) {
                    System.out.println("Quasarzone crawling error: " + e.getMessage());
                }
            }).thenRun(() -> {
                try {
                    webCrawlerService.scrapefmkorea("https://www.fmkorea.com/hotdeal");
                    System.out.println("FMKorea crawling completed");
                } catch (Exception e) {
                    System.out.println("FMKorea crawling error: " + e.getMessage());
                }
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
            }).thenRun(() ->{
               try{
                   notificationService.titleCompare(num);
                   System.out.println("Send Message completed");
               }catch (Exception e) {
                   System.out.println("Send Message error: " + e.getMessage());
               }
            }).join();

            System.out.println("All scheduled crawling completed successfully");
        } catch (Exception e) {
            System.out.println("Scheduled crawling error: " + e.getMessage());
        }
    }
}
