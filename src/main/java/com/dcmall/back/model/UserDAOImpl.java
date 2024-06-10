package com.dcmall.back.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<UserDTO> selectUser() {
        return sqlSessionTemplate.selectList("selectUser");
    }
}
