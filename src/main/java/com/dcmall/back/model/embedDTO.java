package com.dcmall.back.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
public class embedDTO {

    private int num;
    private String title;
    private ArrayList<Double> embedding;

}
