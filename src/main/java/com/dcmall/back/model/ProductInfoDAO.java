package com.dcmall.back.model;

import java.util.List;

public interface ProductInfoDAO {
    int insertProduct(String id, String title, String cost, String url);
    String selectProduct(int id);

}
