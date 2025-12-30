package com.lynn.emergence.controller;

import com.lynn.emergence.entity.Command;
import com.lynn.emergence.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/command")
@CrossOrigin(origins = "*")
public class CommandController {

    @Autowired
    private CommandService commandService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String eventId,
                                    @RequestParam(required = false) String commanderId,
                                    @RequestParam(required = false) String executorId,
                                    @RequestParam(required = false) String status) {
        List<Command> commands;
        if (eventId != null && !eventId.isEmpty()) {
            commands = commandService.findByEventId(eventId);
        } else if (commanderId != null && !commanderId.isEmpty()) {
            commands = commandService.findByCommanderId(commanderId);
        } else if (executorId != null && !executorId.isEmpty()) {
            commands = commandService.findByExecutorId(executorId);
        } else if (status != null && !status.isEmpty()) {
            commands = commandService.findByStatus(status);
        } else {
            commands = commandService.findAll();
        }
        return Map.of("success", true, "data", commands);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        Command command = commandService.findById(id);
        if (command != null) {
            return Map.of("success", true, "data", command);
        }
        return Map.of("success", false, "message", "指令不存在");
    }

    @PostMapping("/issue")
    public Map<String, Object> issue(@RequestBody Command command) {
        try {
            int result = commandService.issueCommand(command);
            if (result > 0) {
                return Map.of("success", true, "message", "指令发布成功", "data", command);
            }
            return Map.of("success", false, "message", "指令发布失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "指令发布失败: " + e.getMessage());
        }
    }

    @PutMapping("/execute/{id}")
    public Map<String, Object> execute(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            String executorId = body.get("executorId");
            String executorName = body.get("executorName");
            int result = commandService.executeCommand(id, executorId, executorName);
            if (result > 0) {
                return Map.of("success", true, "message", "指令执行中");
            }
            return Map.of("success", false, "message", "操作失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "操作失败: " + e.getMessage());
        }
    }

    @PutMapping("/complete/{id}")
    public Map<String, Object> complete(@PathVariable String id) {
        try {
            int result = commandService.completeCommand(id);
            if (result > 0) {
                return Map.of("success", true, "message", "指令已完成");
            }
            return Map.of("success", false, "message", "操作失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "操作失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Command command) {
        try {
            int result = commandService.updateCommand(command);
            if (result > 0) {
                return Map.of("success", true, "message", "更新成功");
            }
            return Map.of("success", false, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败: " + e.getMessage());
        }
    }
}

