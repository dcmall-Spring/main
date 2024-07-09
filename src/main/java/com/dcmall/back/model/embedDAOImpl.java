package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class embedDAOImpl implements embedDAO{

    private final SqlSessionTemplate sqlSession;

    @Autowired
    public embedDAOImpl(@Qualifier("masterSqlSessionTemplate") SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }
    @Override
    public String emdbed() {
        return sqlSession.selectOne("test");
    }
}
