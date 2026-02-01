package com.example.kanban.service;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TaskServiceInterface {
    @Transactional(readOnly = true)
    List<TaskResponseDto> getAllTasks();

    @Transactional(readOnly = true)
    TaskResponseDto getTask(String id);

    TaskResponseDto editTask(String id, TaskRequestDto taskDto, String username);

    TaskResponseDto editPartialTask(String id, TaskPatchRequestDto task, String username);

    void deleteTask(String id, String username);

    void updateStatus(String taskId, String newStatus);
}
