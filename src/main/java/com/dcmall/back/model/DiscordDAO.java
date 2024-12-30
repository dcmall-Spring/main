package com.dcmall.back.model;

public interface DiscordDAO {
    int certification(String word);
    void deleteCheckcode(int num);
}
