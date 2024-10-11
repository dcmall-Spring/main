package com.dcmall.back.model;

import lombok.Data;

@Data
public class UserInfoDTO {
    private int num;
    private String email;
    private String nickname;
    private String access;
    private String resetToken;
    private String resetTokenExpiry;
    private String emailToken;
    private String SessionId;
}
