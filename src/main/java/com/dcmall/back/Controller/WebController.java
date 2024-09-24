package com.dcmall.back.Controller;

import com.dcmall.back.Service.NotificationService;
import com.dcmall.back.Service.WebCrawlerService;
import com.dcmall.back.model.EmbedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 수동으로 크롤링으로 실행하는 WebController
 */
@RestController
public class WebController {
    @Autowired
    public WebCrawlerService webCrawlerService;
    @Autowired
    private EmbedDAO embedDAO;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/scrape")
    public String scrape() {
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
            }).thenRun(() ->{
                try{
                    notificationService.titleCompare(num);
                    System.out.println("num" + num);
                    System.out.println("Send Message completed");
                }catch (Exception e) {
                    System.out.println("Send Message error: " + e.getMessage());
                }
            }).join();

            return "All scheduled crawling completed successfully";
        } catch (Exception e) {
            return "Scheduled crawling error: " + e.getMessage();
        }
    }


}
