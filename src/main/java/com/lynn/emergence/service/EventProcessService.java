package com.lynn.emergence.service;

import com.lynn.emergence.entity.EventProcess;
import com.lynn.emergence.mapper.EventProcessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventProcessService {

    @Autowired
    private EventProcessMapper eventProcessMapper;

    @Transactional(readOnly = true)
    public List<EventProcess> findAll() {
        return eventProcessMapper.findAll();
    }

    @Transactional(readOnly = true)
    public List<EventProcess> findByEventId(String eventId) {
        return eventProcessMapper.findByEventId(eventId);
    }

    @Transactional
    public int recordProcess(EventProcess process) {
        if (process.getId() == null || process.getId().isEmpty()) {
            process.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (process.getProcessTime() == null) {
            process.setProcessTime(LocalDateTime.now());
        }
        return eventProcessMapper.insert(process);
    }
}


