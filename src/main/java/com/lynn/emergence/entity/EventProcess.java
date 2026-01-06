package com.lynn.emergence.entity;

import java.time.LocalDateTime;

/**
 * 事件流程记录表，对事件的上报、指挥、调度、处理等关键节点进行日志化记录
 */
public class EventProcess {
    private String id;
    private String eventId;
    /**
     * 流程类型：report/command/dispatch/handle/resolve/announce 等
     */
    private String processType;
    private String operatorId;
    private String operatorName;
    private LocalDateTime processTime;
    private String processNote;

    public EventProcess() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public LocalDateTime getProcessTime() {
        return processTime;
    }

    public void setProcessTime(LocalDateTime processTime) {
        this.processTime = processTime;
    }

    public String getProcessNote() {
        return processNote;
    }

    public void setProcessNote(String processNote) {
        this.processNote = processNote;
    }
}


