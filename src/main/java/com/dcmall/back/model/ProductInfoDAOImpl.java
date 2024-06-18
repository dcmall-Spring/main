package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ProductInfoDAOImpl implements ProductInfoDAO {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public int insertProduct(String id, String title, String url){
        HashMap<String, String> hashMap = new HashMap<>(){{
            put("id", id);
            put("title", title);
            put("url", url);
        }};

        return sqlSessionTemplate.insert("insertProduct", hashMap);
    }
}
