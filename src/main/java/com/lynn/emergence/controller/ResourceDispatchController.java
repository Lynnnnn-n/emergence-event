package com.lynn.emergence.controller;

import com.lynn.emergence.entity.ResourceDispatch;
import com.lynn.emergence.service.ResourceDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispatch")
@CrossOrigin(origins = "*")
public class ResourceDispatchController {

    @Autowired
    private ResourceDispatchService dispatchService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String eventId,
                                    @RequestParam(required = false) String resourceId,
                                    @RequestParam(required = false) String status) {
        List<ResourceDispatch> dispatches;
        if (eventId != null && !eventId.isEmpty()) {
            dispatches = dispatchService.findByEventId(eventId);
        } else if (resourceId != null && !resourceId.isEmpty()) {
            dispatches = dispatchService.findByResourceId(resourceId);
        } else if (status != null && !status.isEmpty()) {
            dispatches = dispatchService.findByStatus(status);
        } else {
            dispatches = dispatchService.findAll();
        }
        return Map.of("success", true, "data", dispatches);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) {
        ResourceDispatch dispatch = dispatchService.findById(id);
        if (dispatch != null) {
            return Map.of("success", true, "data", dispatch);
        }
        return Map.of("success", false, "message", "调度记录不存在");
    }

    @PostMapping("/dispatch")
    public Map<String, Object> dispatch(@RequestBody ResourceDispatch dispatch) {
        try {
            int result = dispatchService.dispatchResource(dispatch);
            if (result > 0) {
                return Map.of("success", true, "message", "资源调度成功", "data", dispatch);
            }
            return Map.of("success", false, "message", "资源调度失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "资源调度失败: " + e.getMessage());
        }
    }

    @PutMapping("/arrive/{id}")
    public Map<String, Object> arrive(@PathVariable String id) {
        try {
            int result = dispatchService.arriveResource(id);
            if (result > 0) {
                return Map.of("success", true, "message", "资源已到达");
            }
            return Map.of("success", false, "message", "操作失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "操作失败: " + e.getMessage());
        }
    }

    @PutMapping("/complete/{id}")
    public Map<String, Object> complete(@PathVariable String id) {
        try {
            int result = dispatchService.completeDispatch(id);
            if (result > 0) {
                return Map.of("success", true, "message", "调度已完成");
            }
            return Map.of("success", false, "message", "操作失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "操作失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody ResourceDispatch dispatch) {
        try {
            int result = dispatchService.updateDispatch(dispatch);
            if (result > 0) {
                return Map.of("success", true, "message", "更新成功");
            }
            return Map.of("success", false, "message", "更新失败");
        } catch (Exception e) {
            return Map.of("success", false, "message", "更新失败: " + e.getMessage());
        }
    }
}

