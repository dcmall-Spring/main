package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EmbedDAOImpl implements EmbedDAO {
    @Autowired
    @Qualifier("postgreSqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @Override
    public void insertEmbed(String title, ArrayList<Double> embedding, int url, int siteNum) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("embedding", embedding);
            params.put("url", url);
            params.put("siteNum", siteNum);

            sqlSessionTemplate.insert("insertEmbed", params);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception 문제: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<EmbedDTO> selectEmbed(){   //검색을 요청할 때마다 매번 불러오는 건 그런데..
        try{
            return sqlSessionTemplate.selectList("selectEmbed");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("select 문제: "+ e.getMessage());
            throw e;
        }
    }

    @Override
    public Object selectEmbedNum(){
        try {
            return sqlSessionTemplate.selectOne("selectLastNum");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public synchronized boolean isExist(String title) {
        try {
            String result = sqlSessionTemplate.selectOne("isExist", title);
            return result == null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exist 에러: "+e.getMessage());
            return false;
        }
    }
}