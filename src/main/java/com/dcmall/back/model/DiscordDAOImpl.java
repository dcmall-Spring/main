package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DiscordDAOImpl implements DiscordDAO {

    @Autowired
    @Qualifier("mySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int certification(String word) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("word", word);
            return sqlSessionTemplate.selectOne("word",map);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void deleteCheckcode(int num) {
        try {
            sqlSessionTemplate.delete("deleteCheckcode", num);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
