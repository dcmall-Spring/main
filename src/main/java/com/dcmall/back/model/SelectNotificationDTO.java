package com.dcmall.back.model;

import lombok.Data;

@Data
public class SelectNotificationDTO {
    private int userNum;
    private String title;
    private int url;
    private int siteNum;

}
