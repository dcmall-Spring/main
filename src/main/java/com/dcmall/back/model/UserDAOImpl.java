package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final SqlSessionTemplate masterSqlSession;

    @Autowired
    public UserDAOImpl(@Qualifier("masterSqlSessionTemplate") SqlSessionTemplate masterSqlSession){
        this.masterSqlSession = masterSqlSession;
    }

    @Override
    public List<UserDTO> selectUser() {
        return masterSqlSession.selectList("selectUser");
    }
}
