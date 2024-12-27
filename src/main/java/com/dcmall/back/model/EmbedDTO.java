package com.dcmall.back.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class EmbedDTO {

    private int num;
    private String title;
    private ArrayList<Double> embedding;

}
