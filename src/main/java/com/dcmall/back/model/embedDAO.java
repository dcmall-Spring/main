package com.dcmall.back.model;

import java.util.List;

public interface embedDAO {
    void insertEmbed(String title, List<Float> embedding);
}
