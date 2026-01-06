//package com.lynn.emergence.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lynn.emergence.entity.Event;
//import com.lynn.emergence.service.EventService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Map;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * 事件上报功能 Controller 层单元测试
// */
//@WebMvcTest(EventController.class)
//@DisplayName("事件上报 Controller 层测试")
//class EventControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private EventService eventService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Event testEvent;
//
//    @BeforeEach
//    void setUp() {
//        testEvent = new Event();
//        testEvent.setId("test-event-id");
//        testEvent.setTitle("测试事件标题");
//        testEvent.setDescription("测试事件描述");
//        testEvent.setType("medical");
//        testEvent.setLevel("high");
//        testEvent.setLocation("测试地点");
//        testEvent.setReporterId("user001");
//        testEvent.setReporterName("测试用户");
//        testEvent.setStatus("pending");
//        testEvent.setReportTime(LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("测试正常上报事件 - 成功")
//    void testReportEvent_Success() throws Exception {
//        // Given: 模拟服务层返回成功
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        // When & Then: 发送POST请求并验证响应
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("事件上报成功"))
//                .andExpect(jsonPath("$.data.id").value("test-event-id"))
//                .andExpect(jsonPath("$.data.title").value("测试事件标题"));
//
//        // 验证服务层方法被调用
//        verify(eventService, times(1)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 服务层返回失败")
//    void testReportEvent_Failure_ServiceReturnsZero() throws Exception {
//        // Given: 模拟服务层返回失败
//        when(eventService.reportEvent(any(Event.class))).thenReturn(0);
//
//        // When & Then: 发送POST请求并验证响应
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("事件上报失败"));
//
//        verify(eventService, times(1)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 服务层抛出异常")
//    void testReportEvent_Exception() throws Exception {
//        // Given: 模拟服务层抛出异常
//        when(eventService.reportEvent(any(Event.class)))
//                .thenThrow(new RuntimeException("数据库连接失败"));
//
//        // When & Then: 发送POST请求并验证响应
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("事件上报失败: 数据库连接失败"));
//
//        verify(eventService, times(1)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 请求体为空")
//    void testReportEvent_EmptyRequestBody() throws Exception {
//        // When & Then: 发送空请求体
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").exists());
//
//        verify(eventService, times(1)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 验证事件数据传递")
//    void testReportEvent_VerifyDataPassing() throws Exception {
//        // Given
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        // When
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk());
//
//        // Then: 验证传递给服务层的事件数据
//        verify(eventService).reportEvent(argThat(event ->
//                event.getTitle().equals("测试事件标题") &&
//                event.getDescription().equals("测试事件描述") &&
//                event.getType().equals("medical") &&
//                event.getLevel().equals("high") &&
//                event.getLocation().equals("测试地点") &&
//                event.getReporterId().equals("user001") &&
//                event.getReporterName().equals("测试用户")
//        ));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 不同事件类型")
//    void testReportEvent_DifferentEventTypes() throws Exception {
//        // Given: 测试不同类型的事件
//        String[] eventTypes = {"fire", "earthquake", "medical", "security", "other"};
//
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        for (String type : eventTypes) {
//            testEvent.setType(type);
//
//            // When & Then
//            mockMvc.perform(post("/api/event/report")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(testEvent)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true));
//        }
//
//        verify(eventService, times(eventTypes.length)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 不同事件等级")
//    void testReportEvent_DifferentEventLevels() throws Exception {
//        // Given: 测试不同等级的事件
//        String[] eventLevels = {"low", "normal", "high", "urgent"};
//
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        for (String level : eventLevels) {
//            testEvent.setLevel(level);
//
//            // When & Then
//            mockMvc.perform(post("/api/event/report")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(testEvent)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true));
//        }
//
//        verify(eventService, times(eventLevels.length)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 验证响应格式")
//    void testReportEvent_VerifyResponseFormat() throws Exception {
//        // Given
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        // When & Then: 验证响应包含所有必要字段
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").exists())
//                .andExpect(jsonPath("$.message").exists())
//                .andExpect(jsonPath("$.data").exists())
//                .andExpect(jsonPath("$.data.id").exists())
//                .andExpect(jsonPath("$.data.title").exists());
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 缺少必需字段")
//    void testReportEvent_MissingRequiredFields() throws Exception {
//        // Given: 创建一个缺少必需字段的事件
//        Event incompleteEvent = new Event();
//        incompleteEvent.setTitle("只有标题");
//
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        // When & Then: 即使缺少字段，Controller也应该能处理（验证由Service层或数据库层完成）
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(incompleteEvent)))
//                .andExpect(status().isOk());
//
//        verify(eventService, times(1)).reportEvent(any(Event.class));
//    }
//
//    @Test
//    @DisplayName("测试上报事件 - 验证CORS配置")
//    void testReportEvent_CorsConfiguration() throws Exception {
//        // Given
//        when(eventService.reportEvent(any(Event.class))).thenReturn(1);
//
//        // When & Then: 验证CORS头
//        mockMvc.perform(post("/api/event/report")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testEvent))
//                        .header("Origin", "http://localhost:3000"))
//                .andExpect(status().isOk());
//    }
//}
//
//
