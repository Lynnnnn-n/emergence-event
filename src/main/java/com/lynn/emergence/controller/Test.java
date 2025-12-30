package com.lynn.emergence.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 测试控制器
 * 注意：登录功能已迁移到 UserController
 */
@RestController
@RequestMapping("/api/test")
public class Test {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        return Map.of("success", true, "message", "系统运行正常");
    }
}
