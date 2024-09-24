package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ProductInfoDAOImpl implements ProductInfoDAO {
    @Autowired
    @Qualifier("mySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public int insertProduct(String id, String title,String cost, String url){
        HashMap<String, String> hashMap = new HashMap<>(){{
            put("id", id);
            put("title", title);
            put("cost", cost);
            put("url", url);
        }};

        return sqlSessionTemplate.insert("insertProduct", hashMap);
    }

    @Override
    public int insertProductWithImage(String id, String title, String cost, String url, String imageUrl){
        HashMap<String, String> hashMap = new HashMap<>(){{
           put("id", id);
           put("title", title);
           put("cost", cost);
           put("url", url);
           put("imageUrl", imageUrl);
        }};

        return sqlSessionTemplate.insert("insertProductWithImage", hashMap);
    }

    /**
     * 퀘이사존 최상단 데이터를 가져오는 메서드.
     * @return
     */
    @Override
    public String selectProduct(int id) {
        return sqlSessionTemplate.selectOne("selectProduct", id);
    }
}
