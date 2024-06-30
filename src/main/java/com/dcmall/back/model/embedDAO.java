package com.dcmall.back.model;

import java.util.ArrayList;

public interface embedDAO {
    void insertEmbed(String title, ArrayList<Double> embedding);
}
