package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.ResourceDispatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceDispatchMapper {
    ResourceDispatch findById(@Param("id") String id);
    List<ResourceDispatch> findAll();
    List<ResourceDispatch> findByEventId(@Param("eventId") String eventId);
    List<ResourceDispatch> findByResourceId(@Param("resourceId") String resourceId);
    List<ResourceDispatch> findByStatus(@Param("status") String status);
    int insert(ResourceDispatch dispatch);
    int update(ResourceDispatch dispatch);
    int deleteById(@Param("id") String id);
}

