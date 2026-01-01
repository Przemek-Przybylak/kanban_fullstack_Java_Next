package com.example.kanban.controller;

import com.example.kanban.DTO.*;
import com.example.kanban.service.ProjectService;
import com.example.kanban.util.LocationUtil;
import com.example.kanban.validation.OnCreate;
import com.example.kanban.validation.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getTaskByProject(@PathVariable String projectId) {

        return ResponseEntity.ok(projectService.getTaskByProject(projectId));
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskResponseDto> addTask(@PathVariable String projectId, @Validated(OnCreate.class) @RequestBody TaskRequestDto taskDto, Authentication authentication) {
        TaskResponseDto taskResponseDto = projectService.addTask(projectId, taskDto, authentication.getName());

        URI location = LocationUtil.buildLocation(taskResponseDto.id());

        return ResponseEntity.created(location).body(taskResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {

        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable String id) {

        return ResponseEntity.ok(projectService.getProject(id));
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto> addProject(@Validated(OnCreate.class) @RequestBody ProjectRequestDto projectDto, Authentication authentication) {
        ProjectResponseDto savedProject = projectService.addProject(projectDto, authentication.getName());

        URI location = LocationUtil.buildLocation(savedProject.id());

        return ResponseEntity.created(location).body(savedProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> editProject(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody ProjectRequestDto projectDto, Authentication authentication) {

        return ResponseEntity.ok(projectService.editProject(id, projectDto, authentication.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> editPartialProject(@PathVariable String id, @RequestBody ProjectPatchRequestDto projectDto, Authentication authentication) {

        return ResponseEntity.ok(projectService.editPartialProject(id, projectDto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id, Authentication authentication) {

        projectService.deleteProject(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}
