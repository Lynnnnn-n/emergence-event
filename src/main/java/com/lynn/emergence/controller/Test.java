package com.lynn.emergence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class Test {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String user = body.get("username");
        String pass = body.get("password");

        if ("2025001".equals(user) && "123456".equals(pass)) {
            return Map.of("success", true, "message", "OK");
        }
        return Map.of("success", false, "message", "账号或密码错误");
    }
}
