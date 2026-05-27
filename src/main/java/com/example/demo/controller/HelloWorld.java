package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.demo.service.UserService;

@Controller
public class HelloWorld {

    @Autowired
    private UserService userService;

    // /hello エンドポイントは DemoItemController に移行済み

}
