package com.example.kanban.service;

import com.example.kanban.DTO.Mapper;
import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.kanban.util.UpdateIfNotNull.updateIfNotNull;

@Service
public class TaskService implements TaskServiceInterface {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(final TaskRepository taskRepository, final UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> getAllTasks() {
        var allTasks = taskRepository.findAll();

        return allTasks.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponseDto getTask(final String id) {
        final var task = getTaskIfExisting(id);

        return Mapper.toDto(task);
    }

    @Transactional
    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public TaskResponseDto editTask(final String id, final TaskRequestDto taskDto, final String username) {
        Task existingTask = getTaskIfExisting(id);

        final var user = userRepository.findByUsername(taskDto.username()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingTask.setTitle(taskDto.title());
        existingTask.setDescription(taskDto.description());
        existingTask.setStatus(taskDto.status());
        existingTask.setDueDate(taskDto.dueDate());
        existingTask.setApprovedBy(taskDto.approvedBy());
        existingTask.setUser(user);

        final var savedTask = taskRepository.save(existingTask);
        return Mapper.toDto(savedTask);
    }

    @Transactional
    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public TaskResponseDto editPartialTask(final String id, final TaskPatchRequestDto taskDto, final String username) {
        var existingTask = getTaskIfExisting(id);

        updateIfNotNull(taskDto.description(), existingTask::setDescription);
        updateIfNotNull(taskDto.status(), existingTask::setStatus);
        updateIfNotNull(taskDto.approvedBy(), existingTask::setApprovedBy);
        updateIfNotNull(taskDto.dueDate(), existingTask::setDueDate);
        updateIfNotNull(taskDto.title(), existingTask::setTitle);


        final var savedTask = taskRepository.save(existingTask);
        return Mapper.toDto(savedTask);
    }

    @Transactional
    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public void deleteTask(final String id, final String username) {
        final var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        taskRepository.delete(task);
    }

    private Task getTaskIfExisting(final String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
}
