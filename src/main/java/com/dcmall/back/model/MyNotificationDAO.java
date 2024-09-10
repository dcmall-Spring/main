package com.dcmall.back.model;


public interface MyNotificationDAO {
    void insertDiscord(int userNum, String discordNum);
    String selectDiscord(int userNum);
}
