package com.example.kanban.service;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    private UserServiceInterface userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    TaskService taskService;

    String exampleId = "123";

    @Test
    void shouldReturnTaskWhenExists() {
        Task task = new Task();
        task.setId(exampleId);

        when(taskRepository.findById(exampleId))
                .thenReturn(Optional.of(task));

        TaskResponseDto result = taskService.getTask(exampleId);

        assertEquals(exampleId, result.id());
    }

    @Test
    void shouldReturnProjectTasks() {
        Project project1 = new Project();
        project1.setId("p1");

        Project project2 = new Project();
        project2.setId("p2");

        Task task1 = new Task();
        task1.setId("t1");
        task1.setProject(project1);

        Task task2 = new Task();
        task2.setId("t2");
        task2.setProject(project1);

        Task task3 = new Task();
        task3.setId("t3");
        task3.setProject(project2);

        when(taskRepository.findByProjectId("p1"))
                .thenReturn(List.of(task1, task2));

        List<TaskResponseDto> result = taskService.getTasksByProject("p1");

        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowExceptionWhenProjectInTaskNotFound() {
        TaskRequestDto task = new TaskRequestDto("title", "desc", "todo", null, null, "admin", "0");
        String projectId = "p1";

        when(projectService.getProjectIfExisting(projectId))
                .thenThrow(new NotFoundException("project", projectId));

        assertThrows(NotFoundException.class,
                () -> taskService.addTask(projectId, task, "u1"));
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(exampleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.getTask(exampleId));

        assertTrue(exception.getMessage().contains("Not found task"));
    }

    @Test
    void shouldEditTaskPartially() {
        String username = "u1";

        User user = new User();
        user.setUsername(username);

        Task existingTask = new Task();
        existingTask.setId(exampleId);
        existingTask.setTitle("old title");
        existingTask.setUser(user);

        when(taskRepository.findById(exampleId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskPatchRequestDto patchRequest = new TaskPatchRequestDto("new title", null, null, null, null);

        TaskResponseDto result = taskService.editPartialTask(exampleId, patchRequest, username);

        assertEquals("new title", result.title());
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    void shouldDeleteTask() {
        String username = "u1";

        User user = new User();
        user.setUsername(username);

        Task existingTask = new Task();
        existingTask.setId(exampleId);
        existingTask.setUser(user);

        when(taskRepository.findById(exampleId)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(exampleId, username);

        verify(taskRepository).delete(existingTask);
    }

    @Test
    void shouldThrowExceptionWhenTaskToDeleteNotExist() {
        String username = "u1";
        when(taskRepository.findById(exampleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                taskService.deleteTask(exampleId, username)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("not found task"));

        verify(taskRepository, never()).delete(any());
        verify(taskRepository, never()).deleteById(anyString());
    }
}
