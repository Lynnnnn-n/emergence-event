package com.lynn.emergence.service;

import com.lynn.emergence.entity.Resource;
import com.lynn.emergence.mapper.ResourceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 资源服务层简单单元测试
 */
@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private ResourceMapper resourceMapper;

    @InjectMocks
    private ResourceService resourceService;

    private Resource testResource;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setId("res001");
        testResource.setName("测试资源");
        testResource.setType("personnel");
        testResource.setQuantity(10);
        testResource.setAvailableQuantity(8);
        testResource.setStatus("available");
    }

    @Test
    void testFindAll() {
        // 准备数据
        List<Resource> resources = Arrays.asList(testResource);
        when(resourceMapper.findAll()).thenReturn(resources);

        // 执行测试
        List<Resource> result = resourceService.findAll();

        // 验证结果
        assertEquals(1, result.size());
        assertEquals("res001", result.get(0).getId());
        verify(resourceMapper, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // 准备数据
        when(resourceMapper.findById("res001")).thenReturn(testResource);

        // 执行测试
        Resource result = resourceService.findById("res001");

        // 验证结果
        assertNotNull(result);
        assertEquals("res001", result.getId());
        assertEquals("测试资源", result.getName());
        verify(resourceMapper, times(1)).findById("res001");
    }

    @Test
    void testFindById_NotFound() {
        // 准备数据
        when(resourceMapper.findById("notfound")).thenReturn(null);

        // 执行测试
        Resource result = resourceService.findById("notfound");

        // 验证结果
        assertNull(result);
    }

    @Test
    void testFindByType() {
        // 准备数据
        List<Resource> resources = Arrays.asList(testResource);
        when(resourceMapper.findByType("personnel")).thenReturn(resources);

        // 执行测试
        List<Resource> result = resourceService.findByType("personnel");

        // 验证结果
        assertEquals(1, result.size());
        verify(resourceMapper, times(1)).findByType("personnel");
    }

    @Test
    void testFindByStatus() {
        // 准备数据
        List<Resource> resources = Arrays.asList(testResource);
        when(resourceMapper.findByStatus("available")).thenReturn(resources);

        // 执行测试
        List<Resource> result = resourceService.findByStatus("available");

        // 验证结果
        assertEquals(1, result.size());
        verify(resourceMapper, times(1)).findByStatus("available");
    }

    @Test
    void testSave_NewResource() {
        // 准备数据：新资源（没有ID）
        Resource newResource = new Resource();
        newResource.setName("新资源");
        newResource.setQuantity(5);
        newResource.setStatus(null); // 状态为空，应该自动设置

        when(resourceMapper.insert(any(Resource.class))).thenReturn(1);

        // 执行测试
        int result = resourceService.save(newResource);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(newResource.getId()); // 应该自动生成ID
        assertEquals("available", newResource.getStatus()); // 应该自动设置状态
        assertEquals(5, newResource.getAvailableQuantity()); // 应该等于总数
        verify(resourceMapper, times(1)).insert(any(Resource.class));
        verify(resourceMapper, never()).update(any(Resource.class));
    }

    @Test
    void testSave_UpdateResource() {
        // 准备数据：已有ID的资源（更新）
        testResource.setName("更新后的资源");
        when(resourceMapper.update(any(Resource.class))).thenReturn(1);

        // 执行测试
        int result = resourceService.save(testResource);

        // 验证结果
        assertEquals(1, result);
        verify(resourceMapper, times(1)).update(any(Resource.class));
        verify(resourceMapper, never()).insert(any(Resource.class));
    }

    @Test
    void testDeleteById() {
        // 准备数据
        when(resourceMapper.deleteById("res001")).thenReturn(1);

        // 执行测试
        int result = resourceService.deleteById("res001");

        // 验证结果
        assertEquals(1, result);
        verify(resourceMapper, times(1)).deleteById("res001");
    }

    @Test
    void testRecommendBySupportType() {
        // 准备数据
        List<Resource> resources = Arrays.asList(testResource);
        when(resourceMapper.findBySupportTypeAndStatus("medical", "available")).thenReturn(resources);

        // 执行测试
        List<Resource> result = resourceService.recommendBySupportType("medical");

        // 验证结果
        assertEquals(1, result.size());
        verify(resourceMapper, times(1)).findBySupportTypeAndStatus("medical", "available");
    }
}

