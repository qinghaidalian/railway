package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.entity.DemoItem;
import com.example.demo.service.DemoItemService;

import java.util.List;

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
        List<DemoItem> items = demoItemService.getAllItems();
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping("/items")
    public String items(Model model) {
        List<DemoItem> items = demoItemService.getAllItems();
        model.addAttribute("items", items);
        return "index";
    }
}
