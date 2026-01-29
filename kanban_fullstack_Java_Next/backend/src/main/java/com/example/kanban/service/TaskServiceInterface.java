package com.example.kanban.service;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskServiceInterface {
    @Transactional(readOnly = true)
    List<TaskResponseDto> getAllTasks();

    @Transactional(readOnly = true)
    TaskResponseDto getTask(String id);

    @Transactional
    TaskResponseDto editTask(String id, TaskRequestDto taskDto, String username);

    @Transactional
    TaskResponseDto editPartialTask(String id, TaskPatchRequestDto task, String username);

    @Transactional
    void deleteTask(String id, String username);

    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    void updateStatus(String taskId, String newStatus);
}
