package com.example.kanban.service;

import com.example.kanban.DTO.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProjectServiceInterface {

    @Transactional(readOnly = true)
    List<TaskResponseDto> getTasksByProject(String id);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#projectId)")
    TaskResponseDto addTask(String projectId, TaskRequestDto taskDto, String username);

    @Transactional(readOnly = true)
    List<ProjectResponseDto> getAllProjects();

    @Transactional(readOnly = true)
    ProjectResponseDto getProject(String id);

    ProjectResponseDto addProject(ProjectRequestDto project, String username);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    ProjectResponseDto editProject(String id, ProjectRequestDto projectDto, String username);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    ProjectResponseDto editPartialProject(String id, ProjectPatchRequestDto project, String username);

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    void deleteProject(String id);
}
