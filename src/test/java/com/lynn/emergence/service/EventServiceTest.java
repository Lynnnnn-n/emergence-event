package com.lynn.emergence.service;

import com.lynn.emergence.entity.Event;
import com.lynn.emergence.entity.EventProcess;
import com.lynn.emergence.mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 事件上报功能 Service 层单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("事件上报 Service 层测试")
class EventServiceTest {

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventProcessService eventProcessService;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setTitle("测试事件标题");
        testEvent.setDescription("测试事件描述");
        testEvent.setType("medical");
        testEvent.setLevel("high");
        testEvent.setLocation("测试地点");
        testEvent.setReporterId("user001");
        testEvent.setReporterName("测试用户");
    }

    @Test
    @DisplayName("测试正常上报事件 - 自动生成ID和状态")
    void testReportEvent_Success_AutoGenerateIdAndStatus() {
        // Given: 事件没有ID和状态
        testEvent.setId(null);
        testEvent.setStatus(null);
        testEvent.setReportTime(null);

        // Mock: 模拟插入成功
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When: 执行上报
        int result = eventService.reportEvent(testEvent);

        // Then: 验证结果
        assertEquals(1, result, "上报应该成功");
        assertNotNull(testEvent.getId(), "应该自动生成ID");
        assertFalse(testEvent.getId().isEmpty(), "ID不应该为空");
        assertEquals("pending", testEvent.getStatus(), "状态应该默认为pending");
        assertNotNull(testEvent.getReportTime(), "应该自动设置上报时间");

        // 验证调用了 insert 方法
        verify(eventMapper, times(1)).insert(any(Event.class));

        // 验证调用了记录流程方法
        ArgumentCaptor<EventProcess> processCaptor = ArgumentCaptor.forClass(EventProcess.class);
        verify(eventProcessService, times(1)).recordProcess(processCaptor.capture());

        EventProcess capturedProcess = processCaptor.getValue();
        assertEquals(testEvent.getId(), capturedProcess.getEventId());
        assertEquals("report", capturedProcess.getProcessType());
        assertEquals("user001", capturedProcess.getOperatorId());
        assertEquals("测试用户", capturedProcess.getOperatorName());
        assertTrue(capturedProcess.getProcessNote().contains("测试事件标题"));
    }

    @Test
    @DisplayName("测试上报事件 - 已有ID和状态")
    void testReportEvent_Success_WithExistingIdAndStatus() {
        // Given: 事件已有ID和状态
        String existingId = "existing-event-id";
        testEvent.setId(existingId);
        testEvent.setStatus("processing");
        testEvent.setReportTime(LocalDateTime.now().minusHours(1));

        // Mock: 模拟插入成功
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When: 执行上报
        int result = eventService.reportEvent(testEvent);

        // Then: 验证结果
        assertEquals(1, result);
        assertEquals(existingId, testEvent.getId(), "应该保留原有ID");
        assertEquals("processing", testEvent.getStatus(), "应该保留原有状态");

        verify(eventMapper, times(1)).insert(any(Event.class));
        verify(eventProcessService, times(1)).recordProcess(any(EventProcess.class));
    }

    @Test
    @DisplayName("测试上报事件 - 插入失败")
    void testReportEvent_Failure_InsertFailed() {
        // Given: 模拟插入失败
        when(eventMapper.insert(any(Event.class))).thenReturn(0);

        // When: 执行上报
        int result = eventService.reportEvent(testEvent);

        // Then: 验证结果
        assertEquals(0, result, "插入失败应该返回0");

        verify(eventMapper, times(1)).insert(any(Event.class));
        // 插入失败时不应该记录流程
        verify(eventProcessService, never()).recordProcess(any(EventProcess.class));
    }

    @Test
    @DisplayName("测试上报事件 - 验证流程记录内容")
    void testReportEvent_VerifyProcessRecord() {
        // Given
        testEvent.setId("test-event-id");
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When
        eventService.reportEvent(testEvent);

        // Then: 验证流程记录的内容
        ArgumentCaptor<EventProcess> processCaptor = ArgumentCaptor.forClass(EventProcess.class);
        verify(eventProcessService).recordProcess(processCaptor.capture());

        EventProcess process = processCaptor.getValue();
        assertEquals("test-event-id", process.getEventId());
        assertEquals("report", process.getProcessType());
        assertEquals("user001", process.getOperatorId());
        assertEquals("测试用户", process.getOperatorName());
        assertEquals("事件上报：测试事件标题", process.getProcessNote());

    }

    @Test
    @DisplayName("测试上报事件 - 空ID自动生成")
    void testReportEvent_AutoGenerateId_WhenIdIsEmpty() {
        // Given: ID为空字符串
        testEvent.setId("");
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When
        eventService.reportEvent(testEvent);

        // Then: 应该自动生成新ID
        assertNotNull(testEvent.getId());
        assertFalse(testEvent.getId().isEmpty());
        assertFalse(testEvent.getId().contains("-"), "生成的ID不应该包含连字符");
    }

    @Test
    @DisplayName("测试上报事件 - 空状态自动设置为pending")
    void testReportEvent_AutoSetStatus_WhenStatusIsEmpty() {
        // Given: 状态为空字符串
        testEvent.setStatus("");
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When
        eventService.reportEvent(testEvent);

        // Then: 应该自动设置为pending
        assertEquals("pending", testEvent.getStatus());
    }

    @Test
    @DisplayName("测试上报事件 - 自动设置上报时间")
    void testReportEvent_AutoSetReportTime() {
        // Given: 上报时间为空
        testEvent.setReportTime(null);
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When
        LocalDateTime beforeTime = LocalDateTime.now();
        eventService.reportEvent(testEvent);
        LocalDateTime afterTime = LocalDateTime.now();

        // Then: 应该自动设置上报时间，且时间在合理范围内
        assertNotNull(testEvent.getReportTime());
        assertTrue(testEvent.getReportTime().isAfter(beforeTime.minusSeconds(1)));
        assertTrue(testEvent.getReportTime().isBefore(afterTime.plusSeconds(1)));
    }

    @Test
    @DisplayName("测试上报事件 - 完整事件信息")
    void testReportEvent_CompleteEventInfo() {
        // Given: 完整的事件信息
        testEvent.setId("complete-event-id");
        testEvent.setStatus("pending");
        testEvent.setReportTime(LocalDateTime.now());
        when(eventMapper.insert(any(Event.class))).thenReturn(1);

        // When
        int result = eventService.reportEvent(testEvent);

        // Then: 验证所有信息都正确传递
        assertEquals(1, result);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventMapper).insert(eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertEquals("测试事件标题", capturedEvent.getTitle());
        assertEquals("测试事件描述", capturedEvent.getDescription());
        assertEquals("medical", capturedEvent.getType());
        assertEquals("high", capturedEvent.getLevel());
        assertEquals("测试地点", capturedEvent.getLocation());
        assertEquals("user001", capturedEvent.getReporterId());
        assertEquals("测试用户", capturedEvent.getReporterName());
    }
}


