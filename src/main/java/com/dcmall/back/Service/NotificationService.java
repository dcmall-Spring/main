package com.dcmall.back.Service;

import com.dcmall.back.model.MyNotificationDAO;
import com.dcmall.back.model.NotificationDAO;
import com.dcmall.back.model.SelectNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private MyNotificationDAO myNotificationDAO;

    @Autowired
    private DiscordService discordService;



    public void titleCompare(int num){

        List<SelectNotificationDTO> sn = notificationDAO.notifications(num);

        for(SelectNotificationDTO notification : sn){

            sendmessage(notification.getUserNum(), notification.getTitle(), notification.getUrl(), notification.getSiteNum());
        }

    }

    public void sendmessage(int userNum, String title, int url, int siteNum){
        String discordNum = myNotificationDAO.selectDiscord(userNum);
        if(discordNum != null){
            discordService.sendMessage(discordNum, title, url, siteNum);
        }
    }
}
