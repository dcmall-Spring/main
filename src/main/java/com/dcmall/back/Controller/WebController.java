package com.dcmall.back.Controller;

import com.dcmall.back.Service.NotificationService;
import com.dcmall.back.model.EmbedDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 수동으로 크롤링으로 실행하는 WebController
 */
@RestController
public class WebController {
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
        } else if (result instanceof Integer) {
            num = (Integer) result;
        } else {
            num = 0;
        }

        try {
            notificationService.titleCompare(num);
            System.out.println("Send Message completed");
        } catch (Exception e) {
            System.out.println("Send Message error: " + e.getMessage());
        }

        return "success";

    }

}
