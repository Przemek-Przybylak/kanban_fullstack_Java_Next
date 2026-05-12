package com.example.kanban.service;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TaskServiceInterface {

    List<TaskResponseDto> getAllTasks();

    TaskResponseDto getTask(String id);

    List<TaskResponseDto> getTasksByProject(String id);

    TaskResponseDto addTask(String projectId, TaskRequestDto taskDto, String username);

    TaskResponseDto editTask(String id, TaskRequestDto taskDto, String username);

    TaskResponseDto editPartialTask(String id, TaskPatchRequestDto task, String username);

    void deleteTask(String id, String username);

    void updateStatus(String taskId, String newStatus);
}
