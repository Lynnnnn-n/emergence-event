package com.lynn.emergence.mapper;

import com.lynn.emergence.entity.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResourceMapper {
    Resource findById(@Param("id") String id);
    List<Resource> findAll();
    List<Resource> findByType(@Param("type") String type);
    List<Resource> findByStatus(@Param("status") String status);
    List<Resource> findBySupportTypeAndStatus(@Param("supportType") String supportType, @Param("status") String status);
    int insert(Resource resource);
    int update(Resource resource);
    int deleteById(@Param("id") String id);
}

