package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.dto.PageResult;
import com.example.demo.entity.DemoItem;
import com.example.demo.service.DemoItemService;

import java.util.List;
import java.util.Map;

@Controller
public class DemoItemController {

    @Autowired
    private DemoItemService demoItemService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("page", 1);
        return "index";
    }

    @GetMapping("/items")
    public String items(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        model.addAttribute("page", Math.max(page, 1));
        return "index";
    }

    @GetMapping("/api/items")
    @ResponseBody
    public PageResult<DemoItem> pageItems(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "size", defaultValue = "15") int size) {
        return demoItemService.getPage(page, size);
    }
}
