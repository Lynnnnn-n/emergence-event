package com.lynn.emergence.service;

import com.lynn.emergence.entity.User;
import com.lynn.emergence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public User findById(String id) {
        return userMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Transactional
    public int save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString().replace("-", ""));
            return userMapper.insert(user);
        } else {
            return userMapper.update(user);
        }
    }

    @Transactional
    public int deleteById(String id) {
        return userMapper.deleteById(id);
    }
}

