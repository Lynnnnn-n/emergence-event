package com.lynn.emergence.service;

import com.lynn.emergence.entity.Announcement;
import com.lynn.emergence.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Transactional(readOnly = true)
    public List<Announcement> findAll() {
        return announcementMapper.findAll();
    }

    @Transactional(readOnly = true)
    public Announcement findById(String id) {
        return announcementMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Announcement> findByEventId(String eventId) {
        return announcementMapper.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<Announcement> findByStatus(String status) {
        return announcementMapper.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Announcement> findByType(String type) {
        return announcementMapper.findByType(type);
    }

    @Transactional
    public int publish(Announcement announcement) {
        if (announcement.getId() == null || announcement.getId().isEmpty()) {
            announcement.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (announcement.getStatus() == null || announcement.getStatus().isEmpty()) {
            announcement.setStatus("published");
        }
        if (announcement.getType() == null || announcement.getType().isEmpty()) {
            announcement.setType("notice");
        }
        if (announcement.getTargetAudience() == null || announcement.getTargetAudience().isEmpty()) {
            announcement.setTargetAudience("all");
        }
        if (announcement.getViewCount() == null) {
            announcement.setViewCount(0);
        }
        if (announcement.getPublishTime() == null) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        return announcementMapper.insert(announcement);
    }

    @Transactional
    public int update(Announcement announcement) {
        return announcementMapper.update(announcement);
    }

    @Transactional
    public int view(String id) {
        return announcementMapper.incrementViewCount(id);
    }

    @Transactional
    public int deleteById(String id) {
        return announcementMapper.deleteById(id);
    }
}

