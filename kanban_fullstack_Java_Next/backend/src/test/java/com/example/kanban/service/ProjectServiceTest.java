package com.example.kanban.service;

import com.example.kanban.DTO.ProjectPatchRequestDto;
import com.example.kanban.DTO.ProjectResponseDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

        List<TaskResponseDto> result = projectService.getTasksByProject("p1");

        assertEquals(2, result.size());
    }

    @Test
    void shouldAddTask() {
        String projectId = "p1";
        String username = "admin";
        String userId = "user-123";
        TaskRequestDto requestDto = new TaskRequestDto("t1", "desc", "todo", null, null, "admin", "0");

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(List.of(user));

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
        TaskRequestDto task = new TaskRequestDto("title", "desc", "todo", null, null, "admin", "0");

        when(projectRepository.findById("p1")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.addTask("p1", task, "u1"));

        assertTrue(exception.getMessage().contains("Not found project"));
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

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.getProject("123"));

        assertTrue(exception.getMessage().contains("Not found project"));
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

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        ProjectPatchRequestDto changedProject = new ProjectPatchRequestDto("new title", null);

        ProjectResponseDto result = projectService.editPartialProject(projectId, changedProject, username);

        assertEquals("new title", result.title());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void shouldDeleteProject() {
        String projectId = "123";
        String username = "u1";
        Project project = new Project();
        project.setId(projectId);
        project.setUsers(new ArrayList<>());

        SecurityContext securityContext = mock(SecurityContext.class);


        SecurityContextHolder.setContext(securityContext);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.deleteProject(projectId));

        verify(projectRepository).delete(project);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowExceptionWhenProjectToDeleteNotExist() {
        String projectId = "123";
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.deleteProject(projectId));

        assertTrue(exception.getMessage().contains("Not found project"));

        verify(projectRepository, never()).delete(any());
    }
}
