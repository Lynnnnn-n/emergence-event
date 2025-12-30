package com.lynn.emergence.service;

import com.lynn.emergence.entity.Resource;
import com.lynn.emergence.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Transactional(readOnly = true)
    public List<Resource> findAll() {
        return resourceMapper.findAll();
    }

    @Transactional(readOnly = true)
    public Resource findById(String id) {
        return resourceMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Resource> findByType(String type) {
        return resourceMapper.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Resource> findByStatus(String status) {
        return resourceMapper.findByStatus(status);
    }

    @Transactional
    public int save(Resource resource) {
        if (resource.getId() == null || resource.getId().isEmpty()) {
            resource.setId(UUID.randomUUID().toString().replace("-", ""));
            if (resource.getAvailableQuantity() == null) {
                resource.setAvailableQuantity(resource.getQuantity());
            }
            if (resource.getStatus() == null || resource.getStatus().isEmpty()) {
                resource.setStatus("available");
            }
            return resourceMapper.insert(resource);
        } else {
            return resourceMapper.update(resource);
        }
    }

    @Transactional
    public int deleteById(String id) {
        return resourceMapper.deleteById(id);
    }
}

