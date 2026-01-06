package com.lynn.emergence.service;

import com.lynn.emergence.entity.Event;
import com.lynn.emergence.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 简单的服务层测试示例
 */
@ExtendWith(MockitoExtension.class)
class SimpleServiceTest {

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId("ev001");
        testEvent.setTitle("简单测试事件");
        testEvent.setType("medical");
        testEvent.setStatus("pending");
    }

    @Test
    void testFindAllEvents() {
        // 准备：模拟返回事件列表
        List<Event> events = Arrays.asList(testEvent);
        when(eventMapper.findAll()).thenReturn(events);

        // 执行：调用服务方法
        List<Event> result = eventService.findAll();

        // 验证：检查结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ev001", result.get(0).getId());
    }

    @Test
    void testFindEventById() {
        // 准备：模拟根据ID查找事件
        when(eventMapper.findById("ev001")).thenReturn(testEvent);

        // 执行：调用服务方法
        Event result = eventService.findById("ev001");

        // 验证：检查结果
        assertNotNull(result);
        assertEquals("ev001", result.getId());
        assertEquals("简单测试事件", result.getTitle());
    }

    @Test
    void testFindEventById_NotFound() {
        // 准备：模拟找不到事件
        when(eventMapper.findById("notfound")).thenReturn(null);

        // 执行：调用服务方法
        Event result = eventService.findById("notfound");

        // 验证：结果应该为null
        assertNull(result);
    }

    @Test
    void testFindEventsByStatus() {
        // 准备：模拟根据状态查找事件
        List<Event> events = Arrays.asList(testEvent);
        when(eventMapper.findByStatus("pending")).thenReturn(events);

        // 执行：调用服务方法
        List<Event> result = eventService.findByStatus("pending");

        // 验证：检查结果
        assertEquals(1, result.size());
        assertEquals("pending", result.get(0).getStatus());
    }

    @Test
    void testFindEventsByType() {
        // 准备：模拟根据类型查找事件
        List<Event> events = Arrays.asList(testEvent);
        when(eventMapper.findByType("medical")).thenReturn(events);

        // 执行：调用服务方法
        List<Event> result = eventService.findByType("medical");

        // 验证：检查结果
        assertEquals(1, result.size());
        assertEquals("medical", result.get(0).getType());
    }
}

