package com.example.kanban.service;

import com.example.kanban.DTO.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProjectServiceInterface {

    @Transactional
    List<TaskResponseDto> getTasksByProject(String id);

    @Transactional
    TaskResponseDto addTask(String projectId, TaskRequestDto taskDto, String username);

    @Transactional(readOnly = true)
    List<ProjectResponseDto> getAllProjects();

    @Transactional(readOnly = true)
    ProjectResponseDto getProject(String id);

    @Transactional
    ProjectResponseDto addProject(ProjectRequestDto project, String username);

    @Transactional
    ProjectResponseDto editProject(String id, ProjectRequestDto projectDto, String username);

    @Transactional
    ProjectResponseDto editPartialProject(String id, ProjectPatchRequestDto project, String username);

    @Transactional
    void deleteProject(String id, String username);
}
