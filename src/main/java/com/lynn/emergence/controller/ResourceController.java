package com.lynn.emergence.controller;

import com.lynn.emergence.entity.Resource;
import com.lynn.emergence.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource")
@CrossOrigin(origins = "*")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String type,
                                     @RequestParam(required = false) String status) {
        List<Resource> resources;
        if (type != null && !type.isEmpty()) {
            resources = resourceService.findByType(type);
        } else if (status != null && !status.isEmpty()) {
            resources = resourceService.findByStatus(status);
        } else {
            resources = resourceService.findAll();
        }
        return Map.of("success", true, "data", resources);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        Resource resource = resourceService.findById(id);
        if (resource != null) {
            return Map.of("success", true, "data", resource);
        }
        return Map.of("success", false, "message", "资源不存在");
    }

    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Resource resource) {
        try {
            int result = resourceService.save(resource);
            if (result > 0) {
                return Map.of("success", true, "message", "保存成功", "data", resource);
            }
            return Map.of("success", false, "message", "保存失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "保存失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable String id) {
        try {
            int result = resourceService.deleteById(id);
            if (result > 0) {
                return Map.of("success", true, "message", "删除成功");
            }
            return Map.of("success", false, "message", "删除失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "删除失败: " + e.getMessage());
        }
    }
}

