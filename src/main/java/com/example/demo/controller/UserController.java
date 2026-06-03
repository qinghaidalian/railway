package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private static final String SESSION_USER = "loginUser";

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        model.addAttribute("error", error != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        User user = userService.getByUsername(username);
        if (user != null && Boolean.TRUE.equals(user.getEnabled()) && password.equals(user.getPassword())) {
            session.setAttribute(SESSION_USER, user);
            return "redirect:/menu";
        }
        return "redirect:/login?error=1";
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute(SESSION_USER);
        if (loginUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginUser", loginUser);
        return "menu";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_USER);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userForm", new User());
        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userForm", userService.getById(id));
        return "users";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        }
        if (user.getId() != null) {
            userService.updateUser(user);
        } else {
            userService.createUser(user);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute(SESSION_USER) == null) {
            return "redirect:/login";
        }
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
