package com.dcmall.back.model;

import java.util.*;

public interface embedDAO {
    void insertEmbed(String title, ArrayList<Double> embedding, int url, int siteNum);

    List<embedDTO> selectEmbed();

    Object selectEmbedNum();

    boolean isExist(String title);
}
