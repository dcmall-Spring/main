package com.dcmall.back.model;

import lombok.Data;

import java.util.List;

@Data
public class embedDTO {
    private String title;
    private List<Float> embedding;
}
