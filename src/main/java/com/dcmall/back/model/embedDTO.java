package com.dcmall.back.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
public class embedDTO {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private ArrayList<Double> embedding;

}
