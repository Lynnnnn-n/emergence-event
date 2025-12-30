package com.lynn.emergence.controller;

import com.lynn.emergence.entity.Announcement;
import com.lynn.emergence.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcement")
@CrossOrigin(origins = "*")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String eventId,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(required = false) String status) {
        List<Announcement> announcements;
        if (eventId != null && !eventId.isEmpty()) {
            announcements = announcementService.findByEventId(eventId);
        } else if (type != null && !type.isEmpty()) {
            announcements = announcementService.findByType(type);
        } else if (status != null && !status.isEmpty()) {
            announcements = announcementService.findByStatus(status);
        } else {
            announcements = announcementService.findAll();
        }
        return Map.of("success", true, "data", announcements);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        Announcement announcement = announcementService.findById(id);
        if (announcement != null) {
            announcementService.view(id);
            return Map.of("success", true, "data", announcement);
        }
        return Map.of("success", false, "message", "信息不存在");
    }

    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody Announcement announcement) {
        try {
            int result = announcementService.publish(announcement);
            if (result > 0) {
                return Map.of("success", true, "message", "信息发布成功", "data", announcement);
            }
            return Map.of("success", false, "message", "信息发布失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "信息发布失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Announcement announcement) {
        try {
            int result = announcementService.update(announcement);
            if (result > 0) {
                return Map.of("success", true, "message", "更新成功");
            }
            return Map.of("success", false, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable String id) {
        try {
            int result = announcementService.deleteById(id);
            if (result > 0) {
                return Map.of("success", true, "message", "删除成功");
            }
            return Map.of("success", false, "message", "删除失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "删除失败: " + e.getMessage());
        }
    }
}

