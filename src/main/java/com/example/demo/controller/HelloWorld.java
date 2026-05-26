package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.UserService;

@Controller
public class HelloWorld {
		
    @Autowired
    private UserService userService;
    
	@GetMapping("/hello")
	public String test() {
		System.out.println("first start");
//		System.out.println(userService.getAllUsers());
		System.out.println("first end");
		return "index";
	}

}
