package com.lynn.emergence.controller;

import com.lynn.emergence.entity.Event;
import com.lynn.emergence.entity.ResourceDispatch;
import com.lynn.emergence.service.EventService;
import com.lynn.emergence.service.ResourceDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dispatch")
@CrossOrigin(origins = "*")
public class ResourceDispatchController {

    @Autowired
    private ResourceDispatchService dispatchService;
    
    @Autowired
    private EventService eventService;

    /**
     * 获取可调度的事件列表（用于下拉选择）
     * 返回状态为 pending（待处理）或 processing（处理中）的事件
     * 返回格式优化为适合下拉框使用：value=事件ID, label=显示文本
     */
    @GetMapping("/events")
    public Map<String, Object> getDispatchableEvents() {
        // 获取待处理和处理中的事件
        List<Event> pendingEvents = eventService.findByStatus("pending");
        List<Event> processingEvents = eventService.findByStatus("processing");
        
        // 合并并去重（如果有重复）
        List<Event> allEvents = pendingEvents.stream()
                .collect(Collectors.toList());
        allEvents.addAll(processingEvents);
        
        // 转换为适合下拉框的格式
        List<Map<String, Object>> eventOptions = allEvents.stream()
                .map(event -> {
                    Map<String, Object> option = new HashMap<>();
                    // value: 用于提交时传递的事件ID
                    option.put("value", event.getId());
                    // label: 下拉框显示的文本
                    String displayText = String.format("%s [%s] - %s", 
                        event.getTitle() != null ? event.getTitle() : "未命名事件",
                        event.getLocation() != null ? event.getLocation() : "未知地点",
                        event.getStatus() != null ? event.getStatus() : "未知状态");
                    option.put("label", displayText);
                    // 额外信息，方便前端显示详情
                    option.put("id", event.getId());
                    option.put("title", event.getTitle() != null ? event.getTitle() : "");
                    option.put("type", event.getType() != null ? event.getType() : "");
                    option.put("level", event.getLevel() != null ? event.getLevel() : "");
                    option.put("location", event.getLocation() != null ? event.getLocation() : "");
                    option.put("status", event.getStatus() != null ? event.getStatus() : "");
                    return option;
                })
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", eventOptions);
        response.put("count", eventOptions.size());
        return response;
    }

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

    /**
     * 资源调度接口 - 通过下拉框选择事件ID
     * 必须传入 eventId（从下拉框选择的值）
     */
    @PostMapping("/dispatch")
    public Map<String, Object> dispatch(@RequestBody Map<String, Object> request) {
        try {
            // 获取事件ID（必须从下拉框选择）
            String eventId = (String) request.get("eventId");
            
            // 验证事件ID是否提供
            if (eventId == null || eventId.trim().isEmpty()) {
                return Map.of("success", false, "message", "请选择要调度的事件");
            }
            
            // 验证事件是否存在且可调度（状态为pending或processing）
            Event event = eventService.findById(eventId);
            if (event == null) {
                return Map.of("success", false, "message", "事件不存在，请重新选择事件");
            }
            
            // 验证事件状态是否可调度
            String eventStatus = event.getStatus();
            if (!"pending".equals(eventStatus) && !"processing".equals(eventStatus)) {
                return Map.of("success", false, "message", 
                    "该事件状态为 \"" + eventStatus + "\"，无法进行资源调度。只能调度状态为\"待处理\"或\"处理中\"的事件");
            }
            
            // 创建调度对象
            ResourceDispatch dispatch = new ResourceDispatch();
            dispatch.setEventId(eventId);
            dispatch.setResourceId((String) request.get("resourceId"));
            dispatch.setResourceName((String) request.get("resourceName"));
            
            // 处理数量
            Object quantityObj = request.get("dispatchQuantity");
            if (quantityObj instanceof Integer) {
                dispatch.setDispatchQuantity((Integer) quantityObj);
            } else if (quantityObj instanceof String) {
                try {
                    dispatch.setDispatchQuantity(Integer.parseInt((String) quantityObj));
                } catch (NumberFormatException e) {
                    return Map.of("success", false, "message", "调度数量格式错误，请输入数字");
                }
            } else if (quantityObj instanceof Number) {
                dispatch.setDispatchQuantity(((Number) quantityObj).intValue());
            } else {
                dispatch.setDispatchQuantity(1); // 默认数量为1
            }
            
            // 验证数量
            if (dispatch.getDispatchQuantity() == null || dispatch.getDispatchQuantity() <= 0) {
                return Map.of("success", false, "message", "调度数量必须大于0");
            }
            
            dispatch.setDispatcherId((String) request.get("dispatcherId"));
            dispatch.setDispatcherName((String) request.get("dispatcherName"));
            dispatch.setRemark((String) request.get("remark"));
            
            // 执行调度
            int result = dispatchService.dispatchResource(dispatch);
            if (result > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "资源调度成功");
                response.put("data", dispatch);
                response.put("eventInfo", Map.of(
                    "id", event.getId(),
                    "title", event.getTitle() != null ? event.getTitle() : "",
                    "location", event.getLocation() != null ? event.getLocation() : "",
                    "type", event.getType() != null ? event.getType() : "",
                    "level", event.getLevel() != null ? event.getLevel() : ""
                ));
                return response;
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

