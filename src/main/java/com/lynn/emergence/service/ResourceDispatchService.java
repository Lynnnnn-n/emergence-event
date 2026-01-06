package com.lynn.emergence.service;

import com.lynn.emergence.entity.EventProcess;
import com.lynn.emergence.entity.Resource;
import com.lynn.emergence.entity.ResourceDispatch;
import com.lynn.emergence.mapper.ResourceDispatchMapper;
import com.lynn.emergence.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResourceDispatchService {
    @Autowired
    private ResourceDispatchMapper dispatchMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private EventProcessService eventProcessService;

    @Transactional(readOnly = true)
    public List<ResourceDispatch> findAll() {
        return dispatchMapper.findAll();
    }

    @Transactional(readOnly = true)
    public ResourceDispatch findById(String id) {
        return dispatchMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ResourceDispatch> findByEventId(String eventId) {
        return dispatchMapper.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<ResourceDispatch> findByResourceId(String resourceId) {
        return dispatchMapper.findByResourceId(resourceId);
    }

    @Transactional(readOnly = true)
    public List<ResourceDispatch> findByStatus(String status) {
        return dispatchMapper.findByStatus(status);
    }

    @Transactional
    public int dispatchResource(ResourceDispatch dispatch) {
        if (dispatch.getId() == null || dispatch.getId().isEmpty()) {
            dispatch.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (dispatch.getStatus() == null || dispatch.getStatus().isEmpty()) {
            dispatch.setStatus("dispatched");
        }
        if (dispatch.getDispatchTime() == null) {
            dispatch.setDispatchTime(LocalDateTime.now());
        }

        // 更新资源可用数量
        Resource resource = resourceMapper.findById(dispatch.getResourceId());
        if (resource != null && resource.getAvailableQuantity() >= dispatch.getDispatchQuantity()) {
            resource.setAvailableQuantity(resource.getAvailableQuantity() - dispatch.getDispatchQuantity());
            if (resource.getAvailableQuantity() == 0) {
                resource.setStatus("in_use");
            }
            resourceMapper.update(resource);
        } else {
            throw new RuntimeException("资源数量不足");
        }

        int result = dispatchMapper.insert(dispatch);
        if (result > 0) {
            EventProcess process = new EventProcess();
            process.setEventId(dispatch.getEventId());
            process.setProcessType("dispatch");
            process.setOperatorId(dispatch.getDispatcherId());
            process.setOperatorName(dispatch.getDispatcherName());
            process.setProcessNote("调度资源：" + dispatch.getResourceName() + "，数量：" + dispatch.getDispatchQuantity());
            eventProcessService.recordProcess(process);
        }
        return result;
    }

    @Transactional
    public int updateDispatch(ResourceDispatch dispatch) {
        return dispatchMapper.update(dispatch);
    }

    @Transactional
    public int arriveResource(String dispatchId) {
        ResourceDispatch dispatch = dispatchMapper.findById(dispatchId);
        if (dispatch != null) {
            dispatch.setStatus("arrived");
            dispatch.setArrivalTime(LocalDateTime.now());
            int result = dispatchMapper.update(dispatch);
            if (result > 0) {
                EventProcess process = new EventProcess();
                process.setEventId(dispatch.getEventId());
                process.setProcessType("dispatch");
                process.setProcessNote("调度资源已到达现场");
                eventProcessService.recordProcess(process);
            }
            return result;
        }
        return 0;
    }

    @Transactional
    public int completeDispatch(String dispatchId) {
        ResourceDispatch dispatch = dispatchMapper.findById(dispatchId);
        if (dispatch != null) {
            dispatch.setStatus("completed");
            // 释放资源
            Resource resource = resourceMapper.findById(dispatch.getResourceId());
            if (resource != null) {
                resource.setAvailableQuantity(resource.getAvailableQuantity() + dispatch.getDispatchQuantity());
                if (resource.getStatus().equals("in_use") && resource.getAvailableQuantity() > 0) {
                    resource.setStatus("available");
                }
                resourceMapper.update(resource);
            }
            int result = dispatchMapper.update(dispatch);
            if (result > 0) {
                EventProcess process = new EventProcess();
                process.setEventId(dispatch.getEventId());
                process.setProcessType("dispatch");
                process.setProcessNote("调度资源执行完毕并已归还");
                eventProcessService.recordProcess(process);
            }
            return result;
        }
        return 0;
    }

    @Transactional
    public int deleteById(String id) {
        return dispatchMapper.deleteById(id);
    }
}

