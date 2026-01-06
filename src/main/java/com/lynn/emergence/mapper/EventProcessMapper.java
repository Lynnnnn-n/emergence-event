package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.EventProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventProcessMapper {
    EventProcess findById(@Param("id") String id);

    List<EventProcess> findByEventId(@Param("eventId") String eventId);

    List<EventProcess> findAll();

    int insert(EventProcess process);
}


