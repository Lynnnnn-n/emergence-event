package com.lynn.emergence.service;

import com.lynn.emergence.entity.Event;
import com.lynn.emergence.mapper.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventMapper.findAll();
    }

    @Transactional(readOnly = true)
    public Event findById(String id) {
        return eventMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Event> findByReporterId(String reporterId) {
        return eventMapper.findByReporterId(reporterId);
    }

    @Transactional(readOnly = true)
    public List<Event> findByStatus(String status) {
        return eventMapper.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Event> findByType(String type) {
        return eventMapper.findByType(type);
    }

    @Transactional
    public int reportEvent(Event event) {
        if (event.getId() == null || event.getId().isEmpty()) {
            event.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (event.getStatus() == null || event.getStatus().isEmpty()) {
            event.setStatus("pending");
        }
        if (event.getReportTime() == null) {
            event.setReportTime(LocalDateTime.now());
        }
        return eventMapper.insert(event);
    }

    @Transactional
    public int updateEvent(Event event) {
        return eventMapper.update(event);
    }

    @Transactional
    public int handleEvent(String eventId, String handlerId, String handlerName) {
        Event event = eventMapper.findById(eventId);
        if (event != null) {
            event.setHandlerId(handlerId);
            event.setHandlerName(handlerName);
            event.setStatus("processing");
            event.setHandleTime(LocalDateTime.now());
            return eventMapper.update(event);
        }
        return 0;
    }

    @Transactional
    public int resolveEvent(String eventId) {
        Event event = eventMapper.findById(eventId);
        if (event != null) {
            event.setStatus("resolved");
            event.setResolveTime(LocalDateTime.now());
            return eventMapper.update(event);
        }
        return 0;
    }

    @Transactional
    public int deleteById(String id) {
        return eventMapper.deleteById(id);
    }
}

