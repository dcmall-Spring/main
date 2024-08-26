package com.dcmall.back.model;

import lombok.Data;

@Data
public class NotificationDTO {
    private int num;
    private String embedding;
    private String threshold;
}
