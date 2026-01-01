package com.example.kanban.controller;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.service.TaskService;
import com.example.kanban.validation.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {

        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable String id) {

        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> editTask(@PathVariable String id, @Validated(OnUpdate.class) @RequestBody TaskRequestDto taskDto, Authentication authentication) {

        return ResponseEntity.ok(taskService.editTask(id, taskDto, authentication.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDto> editPartialTask(@PathVariable String id, @RequestBody TaskPatchRequestDto taskDto, Authentication authentication) {

        return ResponseEntity.ok(taskService.editPartialTask(id, taskDto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, Authentication authentication) {
        taskService.deleteTask(id, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}

