package com.lynn.emergence.controller;

import com.lynn.emergence.entity.User;
import com.lynn.emergence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userService.login(username, password);
        if (user != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "登录成功");
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("name", user.getName());
            userInfo.put("role", user.getRole());
            userInfo.put("phone", user.getPhone());
            userInfo.put("email", user.getEmail());
            userInfo.put("department", user.getDepartment());
            result.put("user", userInfo);
            return result;
        }
        return Map.of("success", false, "message", "账号或密码错误");
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        try {
            List<User> users = userService.findAll();
            return Map.of("success", true, "data", users);
        } catch (Exception e) {
            return Map.of("success", false, "message", "查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                return Map.of("success", true, "data", user);
            }
            return Map.of("success", false, "message", "用户不存在");
        } catch (Exception e) {
            return Map.of("success", false, "message", "查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public Map<String, Object> getByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user != null) {
                return Map.of("success", true, "data", user);
            }
            return Map.of("success", false, "message", "用户不存在");
        } catch (Exception e) {
            return Map.of("success", false, "message", "查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                return Map.of("success", false, "message", "用户名已存在");
            }

            // 设置默认值
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("student");
            }
            if (user.getStatus() == null) {
                user.setStatus(1);
            }

            int result = userService.save(user);
            if (result > 0) {
                return Map.of("success", true, "message", "注册成功", "data", user);
            }
            return Map.of("success", false, "message", "注册失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "注册失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        try {
            int result = userService.save(user);
            if (result > 0) {
                return Map.of("success", true, "message", "更新成功", "data", user);
            }
            return Map.of("success", false, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/update-password")
    public Map<String, Object> updatePassword(@RequestBody Map<String, String> body) {
        try {
            String userId = body.get("userId");
            String oldPassword = body.get("oldPassword");
            String newPassword = body.get("newPassword");

            User user = userService.findById(userId);
            if (user == null) {
                return Map.of("success", false, "message", "用户不存在");
            }

            if (!oldPassword.equals(user.getPassword())) {
                return Map.of("success", false, "message", "原密码错误");
            }

            user.setPassword(newPassword);
            int result = userService.save(user);
            if (result > 0) {
                return Map.of("success", true, "message", "密码修改成功");
            }
            return Map.of("success", false, "message", "密码修改失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "密码修改失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable String id) {
        try {
            int result = userService.deleteById(id);
            if (result > 0) {
                return Map.of("success", true, "message", "删除成功");
            }
            return Map.of("success", false, "message", "删除失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "删除失败: " + e.getMessage());
        }
    }
}

