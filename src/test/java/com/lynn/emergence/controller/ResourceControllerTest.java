//package com.lynn.emergence.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lynn.emergence.entity.Resource;
//import com.lynn.emergence.service.ResourceService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * 资源控制器简单单元测试
// */
//@WebMvcTest(ResourceController.class)
//class ResourceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ResourceService resourceService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Resource testResource;
//
//    @BeforeEach
//    void setUp() {
//        testResource = new Resource();
//        testResource.setId("res001");
//        testResource.setName("测试资源");
//        testResource.setType("personnel");
//        testResource.setQuantity(10);
//        testResource.setAvailableQuantity(8);
//        testResource.setStatus("available");
//    }
//
//    @Test
//    void testGetAllResources() throws Exception {
//        // 准备数据
//        List<Resource> resources = Arrays.asList(testResource);
//        when(resourceService.findAll()).thenReturn(resources);
//
//        // 执行测试并验证
//        mockMvc.perform(get("/api/resource/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].id").value("res001"));
//
//        verify(resourceService, times(1)).findAll();
//    }
//
//    @Test
//    void testGetResourceById() throws Exception {
//        // 准备数据
//        when(resourceService.findById("res001")).thenReturn(testResource);
//
//        // 执行测试并验证
//        mockMvc.perform(get("/api/resource/res001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value("res001"))
//                .andExpect(jsonPath("$.data.name").value("测试资源"));
//
//        verify(resourceService, times(1)).findById("res001");
//    }
//
//    @Test
//    void testGetResourceById_NotFound() throws Exception {
//        // 准备数据
//        when(resourceService.findById("notfound")).thenReturn(null);
//
//        // 执行测试并验证
//        mockMvc.perform(get("/api/resource/notfound"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("资源不存在"));
//
//        verify(resourceService, times(1)).findById("notfound");
//    }
//
//    @Test
//    void testSaveResource() throws Exception {
//        // 准备数据
//        when(resourceService.save(any(Resource.class))).thenReturn(1);
//
//        // 执行测试并验证
//        mockMvc.perform(post("/api/resource/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testResource)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("保存成功"));
//
//        verify(resourceService, times(1)).save(any(Resource.class));
//    }
//
//    @Test
//    void testSaveResource_Failure() throws Exception {
//        // 准备数据：保存失败
//        when(resourceService.save(any(Resource.class))).thenReturn(0);
//
//        // 执行测试并验证
//        mockMvc.perform(post("/api/resource/save")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testResource)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("保存失败"));
//
//        verify(resourceService, times(1)).save(any(Resource.class));
//    }
//
//    @Test
//    void testDeleteResource() throws Exception {
//        // 准备数据
//        when(resourceService.deleteById("res001")).thenReturn(1);
//
//        // 执行测试并验证
//        mockMvc.perform(delete("/api/resource/res001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("删除成功"));
//
//        verify(resourceService, times(1)).deleteById("res001");
//    }
//
//    @Test
//    void testGetResourcesByType() throws Exception {
//        // 准备数据
//        List<Resource> resources = Arrays.asList(testResource);
//        when(resourceService.findByType("personnel")).thenReturn(resources);
//
//        // 执行测试并验证
//        mockMvc.perform(get("/api/resource/list?type=personnel"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray());
//
//        verify(resourceService, times(1)).findByType("personnel");
//    }
//
//    @Test
//    void testGetResourcesByStatus() throws Exception {
//        // 准备数据
//        List<Resource> resources = Arrays.asList(testResource);
//        when(resourceService.findByStatus("available")).thenReturn(resources);
//
//        // 执行测试并验证
//        mockMvc.perform(get("/api/resource/list?status=available"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true));
//
//        verify(resourceService, times(1)).findByStatus("available");
//    }
//}
//
