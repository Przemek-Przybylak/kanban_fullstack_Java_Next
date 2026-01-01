package com.example.kanban.service;

import com.example.kanban.DTO.ProjectPatchRequestDto;
import com.example.kanban.DTO.ProjectResponseDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ProjectService projectService;

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

        List<TaskResponseDto> result = projectService.getTaskByProject("p1");

        assertEquals(2, result.size());
    }

    @Test
    void shouldAddTask() {
        String projectId = "p1";
        String username = "admin";
        String userId = "user-123";
        TaskRequestDto requestDto = new TaskRequestDto("t1", "desc", "todo", null, null);

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(List.of(user));

        when(userService.getUserIdFromUsername(username)).thenReturn(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        TaskResponseDto result = projectService.addTask(projectId, requestDto, username);

        assertNotNull(result);
        assertEquals("t1", result.title());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenProjectInTaskNotFound() {
        TaskRequestDto task = new TaskRequestDto("title", "desc", "todo", null, null);

        when(projectRepository.findById("p1"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> projectService.addTask("p1", task, "u1"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldReturnProjectWhenExist() {
        Project project = new Project();
        project.setId("123");

        when(projectRepository.findById("123"))
                .thenReturn(Optional.of(project));

        ProjectResponseDto result = projectService.getProject("123");

        assertEquals("123", result.id());
    }

    @Test
    void shouldThrowExceptionWhenProjectFound() {
        when(projectRepository.findById("123"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> projectService.getProject("123"));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldEditProjectPartially() {
        String projectId = "123";
        String username = "u1";
        String userId = "user-abc";

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setTitle("old title");
        existingProject.setUsers(List.of(user));
        existingProject.setTasks(new ArrayList<>());

        when(userService.getUserIdFromUsername(username)).thenReturn(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        ProjectPatchRequestDto changedProject = new ProjectPatchRequestDto("new title", null);

        ProjectResponseDto result = projectService.editPartialProject(projectId, changedProject, username);

        assertEquals("new title", result.title());
        verify(projectRepository).save(any(Project.class));
        verify(userService).getUserIdFromUsername(username);
    }

    @Test
    void shouldDeleteProject() {
        Project project = new Project();
        project.setId("123");

        User user = new User();
        user.setUsername("u1");

        project.setUsers(List.of(user));

        when(projectRepository.findById("123"))
                .thenReturn(Optional.of(project));

        projectService.deleteProject("123", user.getUsername());

        verify(projectRepository).delete(project);
    }

    @Test
    void shouldThrowExceptionWhenProjectToDeleteNotExist() {
        String projectId = "123";
        String username = "u1";

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class,
                        () -> projectService.deleteProject(projectId, username));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Project not found", exception.getReason());

        verify(projectRepository, never()).delete(any());
        verify(projectRepository, never()).deleteById(anyString());
    }
}
