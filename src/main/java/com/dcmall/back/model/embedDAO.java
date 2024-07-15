package com.dcmall.back.model;

import java.util.*;

public interface embedDAO {
    void insertEmbed(String title, ArrayList<Double> embedding);

    List<embedDTO> selectEmbed();

    String isExist(String title);
}
