package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findAll();
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") Long id);
}
