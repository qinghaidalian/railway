package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    public User getById(Long id) {
        return userMapper.findById(id);
    }

    public User getByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public int createUser(User user) {
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        return userMapper.insert(user);
    }

    public int updateUser(User user) {
        return userMapper.update(user);
    }

    public int deleteUser(Long id) {
        return userMapper.deleteById(id);
    }
}
