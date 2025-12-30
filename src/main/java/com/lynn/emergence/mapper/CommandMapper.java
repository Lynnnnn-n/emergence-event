package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.Command;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommandMapper {
    Command findById(@Param("id") String id);
    List<Command> findAll();
    List<Command> findByEventId(@Param("eventId") String eventId);
    List<Command> findByCommanderId(@Param("commanderId") String commanderId);
    List<Command> findByExecutorId(@Param("executorId") String executorId);
    List<Command> findByStatus(@Param("status") String status);
    int insert(Command command);
    int update(Command command);
    int deleteById(@Param("id") String id);
}

