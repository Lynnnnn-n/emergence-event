package com.lynn.emergence.service;

import com.lynn.emergence.entity.Command;
import com.lynn.emergence.mapper.CommandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommandService {
    @Autowired
    private CommandMapper commandMapper;

    @Transactional(readOnly = true)
    public List<Command> findAll() {
        return commandMapper.findAll();
    }

    @Transactional(readOnly = true)
    public Command findById(String id) {
        return commandMapper.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Command> findByEventId(String eventId) {
        return commandMapper.findByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<Command> findByCommanderId(String commanderId) {
        return commandMapper.findByCommanderId(commanderId);
    }

    @Transactional(readOnly = true)
    public List<Command> findByExecutorId(String executorId) {
        return commandMapper.findByExecutorId(executorId);
    }

    @Transactional(readOnly = true)
    public List<Command> findByStatus(String status) {
        return commandMapper.findByStatus(status);
    }

    @Transactional
    public int issueCommand(Command command) {
        if (command.getId() == null || command.getId().isEmpty()) {
            command.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (command.getStatus() == null || command.getStatus().isEmpty()) {
            command.setStatus("pending");
        }
        if (command.getPriority() == null || command.getPriority().isEmpty()) {
            command.setPriority("normal");
        }
        if (command.getIssueTime() == null) {
            command.setIssueTime(LocalDateTime.now());
        }
        return commandMapper.insert(command);
    }

    @Transactional
    public int updateCommand(Command command) {
        return commandMapper.update(command);
    }

    @Transactional
    public int executeCommand(String commandId, String executorId, String executorName) {
        Command command = commandMapper.findById(commandId);
        if (command != null) {
            command.setExecutorId(executorId);
            command.setExecutorName(executorName);
            command.setStatus("executing");
            command.setExecuteTime(LocalDateTime.now());
            return commandMapper.update(command);
        }
        return 0;
    }

    @Transactional
    public int completeCommand(String commandId) {
        Command command = commandMapper.findById(commandId);
        if (command != null) {
            command.setStatus("completed");
            command.setCompleteTime(LocalDateTime.now());
            return commandMapper.update(command);
        }
        return 0;
    }

    @Transactional
    public int deleteById(String id) {
        return commandMapper.deleteById(id);
    }
}

