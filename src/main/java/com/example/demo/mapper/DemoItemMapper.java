package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.entity.DemoItem;

import java.util.List;

@Mapper
public interface DemoItemMapper {
    List<DemoItem> findAll();
}
