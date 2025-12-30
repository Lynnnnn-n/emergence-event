package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    Announcement findById(@Param("id") String id);
    List<Announcement> findAll();
    List<Announcement> findByEventId(@Param("eventId") String eventId);
    List<Announcement> findByStatus(@Param("status") String status);
    List<Announcement> findByType(@Param("type") String type);
    int insert(Announcement announcement);
    int update(Announcement announcement);
    int deleteById(@Param("id") String id);
    int incrementViewCount(@Param("id") String id);
}

