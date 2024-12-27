package com.dcmall.back.model;

import java.util.*;

public interface EmbedDAO {
    void insertEmbed(String title, ArrayList<Double> embedding, int url, int siteNum);

    List<EmbedDTO> selectEmbed();

    Object selectEmbedNum();

    boolean isExist(String title);
}
