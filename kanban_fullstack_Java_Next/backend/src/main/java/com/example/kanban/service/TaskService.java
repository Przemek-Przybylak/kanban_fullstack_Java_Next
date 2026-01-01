package com.example.kanban.service;

import com.example.kanban.DTO.Mapper;
import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.kanban.util.UpdateIfNotNull.updateIfNotNull;

@Service
public class TaskService implements TaskServiceInterface {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponseDto> getAllTasks() {
        List<Task> allTasks = taskRepository.findAll();

        return allTasks.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TaskResponseDto getTask(String id) {
        Task task = getTaskIfExisting(id);

        return Mapper.toDto(task);
    }

    @Transactional
    @Override
    public TaskResponseDto editTask(String id, TaskRequestDto taskDto, String username) {
        Task existingTask = getTaskIfExisting(id);

        checkTaskMembership(username, existingTask);

        existingTask.setTitle(taskDto.title());
        existingTask.setDescription(taskDto.description());
        existingTask.setStatus(taskDto.status());
        existingTask.setDueDate(taskDto.dueDate());
        existingTask.setApprovedBy(taskDto.approvedBy());

        Task savedTask = taskRepository.save(existingTask);
        return Mapper.toDto(savedTask);
    }

    @Transactional
    @Override
    public TaskResponseDto editPartialTask(String id, TaskPatchRequestDto taskDto, String username) {
        Task existingTask = getTaskIfExisting(id);

        checkTaskMembership(username, existingTask);

        updateIfNotNull(taskDto.description(), existingTask::setDescription);
        updateIfNotNull(taskDto.status(), existingTask::setStatus);
        updateIfNotNull(taskDto.approvedBy(), existingTask::setApprovedBy);
        updateIfNotNull(taskDto.dueDate(), existingTask::setDueDate);
        updateIfNotNull(taskDto.title(), existingTask::setTitle);


        Task savedTask = taskRepository.save(existingTask);
        return Mapper.toDto(savedTask);
    }

    @Transactional
    @Override
    public void deleteTask(String id, String username) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        checkTaskMembership(username, task);

        taskRepository.delete(task);
    }

    private Task getTaskIfExisting(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    private void checkTaskMembership(String username, Task task) {

        if (task.getUser() == null || !task.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You don't have access to this task");
        }
    }
}
