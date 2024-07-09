package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SiteDAOImpl implements SiteDAO{
    private final SqlSessionTemplate masterSqlSession;

    @Autowired
    public SiteDAOImpl(@Qualifier("masterSqlSessionTemplate") SqlSessionTemplate masterSqlSession){
        this.masterSqlSession = masterSqlSession;
    }

    @Override
    public List<SiteDTO> SelectSite(int id) {
        return masterSqlSession.selectList("selectUser");
    }
}
