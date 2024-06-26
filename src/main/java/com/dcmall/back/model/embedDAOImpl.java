package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class embedDAOImpl implements embedDAO {
    @Autowired
    @Qualifier("postgreSqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void insertEmbed(String title, List<Float> embedding) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("embedding", embedding);

        sqlSessionTemplate.insert("com.dcmall.back.model.embedDAO.insertEmbed", params);
    }
}
