package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class MyNotificationDAOImpl implements MyNotificationDAO {
    @Autowired
    @Qualifier("mySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public void insertDiscord(int userNum, String discordNum) {
        System.out.println("test1 : " + userNum);
        System.out.println("test2 : " + discordNum);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userNum", userNum);
        map.put("discordNum", discordNum);
        sqlSessionTemplate.insert("insertDiscord", map);
    }
    @Override
    public String selectDiscord(int userNum) {
        try{
            return sqlSessionTemplate.selectOne("selectDiscord", userNum);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
