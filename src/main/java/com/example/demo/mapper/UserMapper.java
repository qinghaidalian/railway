package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.User;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    // 1. 查询所有用户
    List<User> findAll();
}
