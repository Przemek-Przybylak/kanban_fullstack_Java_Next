package com.example.kanban.service;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void shouldReturnTaskWhenExists() {
        Task task = new Task();
        task.setId("123");

        when(taskRepository.findById("123"))
                .thenReturn(Optional.of(task));

        TaskResponseDto result = taskService.getTask("123");

        assertEquals("123", result.id());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById("123"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> taskService.getTask("123"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldEditTaskPartially() {
        String username = "u1";
        String taskId = "123";

        User user = new User();
        user.setUsername(username);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("old title");
        existingTask.setUser(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskPatchRequestDto patchRequest = new TaskPatchRequestDto("new title", null, null, null, null);

        TaskResponseDto result = taskService.editPartialTask(taskId, patchRequest, username);

        assertEquals("new title", result.title());
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    void shouldDeleteTask() {
        String username = "u1";
        String taskId = "task-123";

        User user = new User();
        user.setUsername(username);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setUser(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(taskId, username);

        verify(taskRepository).delete(existingTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskToDeleteNotExist() {
        String taskId = "123";
        String username = "u1";

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                taskService.deleteTask(taskId, username)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Task not found", exception.getReason());

        verify(taskRepository, never()).delete(any());
        verify(taskRepository, never()).deleteById(anyString());
    }
}
