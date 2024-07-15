package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class embedDAOImpl implements embedDAO {
    @Autowired
    @Qualifier("postgreSqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public void insertEmbed(String title, ArrayList<Double> embedding) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("embedding", embedding);

            System.out.println("Inserting data: " + params);
            sqlSessionTemplate.insert("insertEmbed", params);
            System.out.println("Insert completed successfully.");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception 문제: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<embedDTO> selectEmbed(){   //검색을 요청할 때마다 매번 불러오는 건 그런데..
        try{
            return sqlSessionTemplate.selectList("selectEmbed");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("select 문제: "+ e.getMessage());
            throw e;
        }
    }

    @Override
    public String isExist(String title){
        try{
            return sqlSessionTemplate.selectOne("isExist", title);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("isExist 문제: "+ e.getMessage());
            throw e;
        }
    }
}