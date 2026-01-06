package com.lynn.emergence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 事件实体类简单测试
 */
class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void testEventCreation() {
        // 测试创建事件对象
        assertNotNull(event);
    }

    @Test
    void testGettersAndSetters() {
        // 测试所有getter和setter方法
        String id = "ev001";
        String title = "测试事件";
        String description = "这是一个测试事件";
        String type = "medical";
        String level = "high";
        String location = "测试地点";
        String reporterId = "user001";
        String reporterName = "测试用户";
        String status = "pending";
        LocalDateTime now = LocalDateTime.now();

        event.setId(id);
        event.setTitle(title);
        event.setDescription(description);
        event.setType(type);
        event.setLevel(level);
        event.setLocation(location);
        event.setReporterId(reporterId);
        event.setReporterName(reporterName);
        event.setStatus(status);
        event.setReportTime(now);

        assertEquals(id, event.getId());
        assertEquals(title, event.getTitle());
        assertEquals(description, event.getDescription());
        assertEquals(type, event.getType());
        assertEquals(level, event.getLevel());
        assertEquals(location, event.getLocation());
        assertEquals(reporterId, event.getReporterId());
        assertEquals(reporterName, event.getReporterName());
        assertEquals(status, event.getStatus());
        assertEquals(now, event.getReportTime());
    }

    @Test
    void testEventWithAllFields() {
        // 测试完整的事件对象
        event.setId("ev001");
        event.setTitle("火灾事件");
        event.setDescription("实验室发生火灾");
        event.setType("fire");
        event.setLevel("urgent");
        event.setLocation("理科楼A区302");
        event.setReporterId("user001");
        event.setReporterName("张三");
        event.setStatus("pending");
        event.setReportTime(LocalDateTime.now());

        assertNotNull(event.getId());
        assertNotNull(event.getTitle());
        assertNotNull(event.getType());
        assertNotNull(event.getLevel());
        assertEquals("pending", event.getStatus());
    }

    @Test
    void testEventWithNullValues() {
        // 测试空值处理
        event.setId(null);
        event.setTitle(null);
        event.setStatus(null);

        assertNull(event.getId());
        assertNull(event.getTitle());
        assertNull(event.getStatus());
    }

    @Test
    void testEventTimeFields() {
        // 测试时间字段
        LocalDateTime reportTime = LocalDateTime.now();
        LocalDateTime handleTime = LocalDateTime.now().plusHours(1);
        LocalDateTime resolveTime = LocalDateTime.now().plusHours(2);

        event.setReportTime(reportTime);
        event.setHandleTime(handleTime);
        event.setResolveTime(resolveTime);

        assertEquals(reportTime, event.getReportTime());
        assertEquals(handleTime, event.getHandleTime());
        assertEquals(resolveTime, event.getResolveTime());
    }
}

