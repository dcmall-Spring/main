package com.dcmall.back.model;

import lombok.Data;

@Data
public class ProductInfoDTO {
    private int postid;
    private int id;
    private String title;
    private int cost;
    private String reply;
    private int like;
    private String url;
    private String imageUrl;
}
