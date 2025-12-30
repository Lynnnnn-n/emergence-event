package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findById(@Param("id") String id);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") String id);
}

