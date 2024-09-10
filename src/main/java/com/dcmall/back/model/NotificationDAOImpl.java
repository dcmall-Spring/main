package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class NotificationDAOImpl implements NotificationDAO {

    @Autowired
    @Qualifier("postgreSqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<SelectNotificationDTO> notifications(int num){
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("num", num);
            return sqlSessionTemplate.selectList("selectNotification", map);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
