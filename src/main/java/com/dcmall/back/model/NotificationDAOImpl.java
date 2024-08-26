package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class NotificationDAOImpl implements NotificationDAO {

    @Autowired
    @Qualifier("postgreSqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<Integer> notifications(int num){
        try {
            return sqlSessionTemplate.selectList("selectNotification", num);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
