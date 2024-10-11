package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserInfoDAOImpl implements  UserInfoDAO {

    @Autowired
    @Qualifier("mySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    @Override
    public String SelectEmail(int num) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("num", num);
        return sqlSessionTemplate.selectOne("selectEmail", map);
    }

}
