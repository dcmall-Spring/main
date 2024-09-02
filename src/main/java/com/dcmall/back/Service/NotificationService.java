package com.dcmall.back.Service;

import com.dcmall.back.model.NotificationDAO;
import com.dcmall.back.model.SelectNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    public void titleCompare(int num){

        List<SelectNotificationDTO> sn = notificationDAO.notifications(num);

        for(SelectNotificationDTO notification : sn){
            System.out.println(notification.getUserNum());
            System.out.println(notification.getTitle());
            System.out.println(notification.getUrl());
            System.out.println(notification.getSiteNum());

            sendmessage(notification.getUserNum(), notification.getTitle(), notification.getUrl(), notification.getSiteNum());
        }



    }

    public void sendmessage(int userNum, String title, int url, int siteNum){


        
    }
}
