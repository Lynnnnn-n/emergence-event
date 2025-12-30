package com.lynn.emergence.controller;

import com.lynn.emergence.entity.Event;
import com.lynn.emergence.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String status,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String reporterId) {
        List<Event> events;
        if (reporterId != null && !reporterId.isEmpty()) {
            events = eventService.findByReporterId(reporterId);
        } else if (status != null && !status.isEmpty()) {
            events = eventService.findByStatus(status);
        } else if (type != null && !type.isEmpty()) {
            events = eventService.findByType(type);
        } else {
            events = eventService.findAll();
        }
        return Map.of("success", true, "data", events);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        Event event = eventService.findById(id);
        if (event != null) {
            return Map.of("success", true, "data", event);
        }
        return Map.of("success", false, "message", "事件不存在");
    }

    @PostMapping("/report")
    public Map<String, Object> report(@RequestBody Event event) {
        try {
            int result = eventService.reportEvent(event);
            if (result > 0) {
                return Map.of("success", true, "message", "事件上报成功", "data", event);
            }
            return Map.of("success", false, "message", "事件上报失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "事件上报失败: " + e.getMessage());
        }
    }

    @PutMapping("/handle/{id}")
    public Map<String, Object> handle(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            String handlerId = body.get("handlerId");
            String handlerName = body.get("handlerName");
            int result = eventService.handleEvent(id, handlerId, handlerName);
            if (result > 0) {
                return Map.of("success", true, "message", "事件处理成功");
            }
            return Map.of("success", false, "message", "事件处理失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "事件处理失败: " + e.getMessage());
        }
    }

    @PutMapping("/resolve/{id}")
    public Map<String, Object> resolve(@PathVariable String id) {
        try {
            int result = eventService.resolveEvent(id);
            if (result > 0) {
                return Map.of("success", true, "message", "事件已解决");
            }
            return Map.of("success", false, "message", "操作失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "操作失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Map<String, Object> body) {
        try {
            String eventId = (String) body.get("id");
            String reporterId = (String) body.get("reporterId");
            
            // 检查事件是否存在
            Event existingEvent = eventService.findById(eventId);
            if (existingEvent == null) {
                return Map.of("success", false, "message", "事件不存在");
            }
            
            // 检查权限：只能修改自己上报的事件，且状态为pending
            if (!existingEvent.getReporterId().equals(reporterId)) {
                return Map.of("success", false, "message", "无权修改此事件");
            }
            
            if (!"pending".equals(existingEvent.getStatus())) {
                return Map.of("success", false, "message", "只能修改待处理状态的事件");
            }
            
            // 更新事件信息
            existingEvent.setTitle((String) body.get("title"));
            existingEvent.setDescription((String) body.get("description"));
            existingEvent.setType((String) body.get("type"));
            existingEvent.setLevel((String) body.get("level"));
            existingEvent.setLocation((String) body.get("location"));
            
            int result = eventService.updateEvent(existingEvent);
            if (result > 0) {
                return Map.of("success", true, "message", "更新成功", "data", existingEvent);
            }
            return Map.of("success", false, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable String id, @RequestParam String reporterId) {
        try {
            // 检查事件是否存在
            Event existingEvent = eventService.findById(id);
            if (existingEvent == null) {
                return Map.of("success", false, "message", "事件不存在");
            }
            
            // 检查权限：只能删除自己上报的事件，且状态为pending
            if (!existingEvent.getReporterId().equals(reporterId)) {
                return Map.of("success", false, "message", "无权删除此事件");
            }
            
            if (!"pending".equals(existingEvent.getStatus())) {
                return Map.of("success", false, "message", "只能删除待处理状态的事件");
            }
            
            int result = eventService.deleteById(id);
            if (result > 0) {
                return Map.of("success", true, "message", "删除成功");
            }
            return Map.of("success", false, "message", "删除失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "删除失败: " + e.getMessage());
        }
    }
}

