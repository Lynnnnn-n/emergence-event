package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    Event findById(@Param("id") String id);
    List<Event> findAll();
    List<Event> findByReporterId(@Param("reporterId") String reporterId);
    List<Event> findByStatus(@Param("status") String status);
    List<Event> findByType(@Param("type") String type);
    List<Event> findByLevel(@Param("level") String level);
    List<Event> findByTypeAndLevel(@Param("type") String type, @Param("level") String level);
    List<Event> findByTitleLike(@Param("title") String title);
    int insert(Event event);
    int update(Event event);
    int deleteById(@Param("id") String id);
}

