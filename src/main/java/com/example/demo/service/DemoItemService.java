package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.PageResult;
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

    public PageResult<DemoItem> getPage(int page, int size) {
        int currentPage = Math.max(page, 1);
        int pageSize = Math.max(size, 1);
        int offset = (currentPage - 1) * pageSize;
        List<DemoItem> items = demoItemMapper.findPage(offset, pageSize);
        long total = demoItemMapper.countAll();
        return new PageResult<>(items, total, currentPage, pageSize);
    }
}
