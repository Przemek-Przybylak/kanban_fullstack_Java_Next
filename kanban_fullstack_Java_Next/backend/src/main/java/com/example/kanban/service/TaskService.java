package com.example.kanban.service;

import com.example.kanban.DTO.Mapper;
import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.Project;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.kanban.util.UpdateIfNotNull.updateIfNotNull;

@Service
@Transactional(readOnly = true)
public class TaskService implements TaskServiceInterface {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectService projectService;

    public TaskService(final TaskRepository taskRepository, final UserRepository userRepository, final ProjectService projectService, final UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        var allTasks = taskRepository.findAll();

        return allTasks.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Override
    @PreAuthorize("@guard.canAccessTask(#id)")
    public TaskResponseDto getTask(final String id) {
        final var task = getTaskIfExisting(id);

        return Mapper.toDto(task);
    }

    @Override
    @PreAuthorize("@guard.canAccessProject(#id)")
    public List<TaskResponseDto> getTasksByProject(final String id) {

        return taskRepository.findByProjectId(id).stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#projectId)")
    public TaskResponseDto addTask(final String projectId, final TaskRequestDto taskDto, final String username) {
        final Project project = projectService.getProjectIfExisting(projectId);

        var task = Mapper.fromDto(taskDto);

        final var user = userService.getOwner(taskDto.username());

        task.setUser(user);
        task.setProject(project);

        taskRepository.save(task);

        return Mapper.toDto(task);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessTask(#id)")
    public TaskResponseDto editTask(final String id, final TaskRequestDto taskDto, final String username) {
        Task existingTask = getTaskIfExisting(id);

        final var user = userRepository.findByUsername(taskDto.username()).orElseThrow(() -> new NotFoundException("user", "usrname: " + taskDto.username()));

        existingTask.setTitle(taskDto.title());
        existingTask.setDescription(taskDto.description());
        existingTask.setStatus(taskDto.status());
        existingTask.setDueDate(taskDto.dueDate());
        existingTask.setApprovedBy(taskDto.approvedBy());
        existingTask.setUser(user);

        final var savedTask = taskRepository.save(existingTask);
        return Mapper.toDto(savedTask);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessTask(#id)")
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

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessTask(#id)")
    public void deleteTask(final String id, final String username) {
        final var task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("task", "id: " + id));

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessTask(#taskId)")
    public void updateStatus(final String taskId, final String newStatus) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("task", "id: " + taskId));

        task.setStatus(newStatus);

        taskRepository.save(task);
    }

    private Task getTaskIfExisting(final String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("task", "id: " + id));
    }
}