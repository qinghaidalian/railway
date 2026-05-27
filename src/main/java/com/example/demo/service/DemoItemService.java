package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.DemoItem;
import com.example.demo.mapper.DemoItemMapper;

import java.util.List;

@Service
public class DemoItemService {

    @Autowired
    private DemoItemMapper demoItemMapper;

    public List<DemoItem> getAllItems() {
        return demoItemMapper.findAll();
    }
}
