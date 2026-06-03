package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.entity.DemoItem;

import java.util.List;

@Mapper
public interface DemoItemMapper {
    List<DemoItem> findAll();
    List<DemoItem> findPage(@Param("offset") int offset, @Param("limit") int limit);
    long countAll();
}
