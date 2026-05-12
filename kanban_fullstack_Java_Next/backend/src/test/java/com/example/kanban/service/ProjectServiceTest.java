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
import org.junit.jupiter.api.BeforeEach;
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

    @InjectMocks
    ProjectService projectService;

    String exampleId = "123";

    @Test
    void shouldReturnProjectWhenExist() {
        Project project = new Project();
        project.setId(exampleId);

        when(projectRepository.findById(exampleId))
                .thenReturn(Optional.of(project));

        ProjectResponseDto result = projectService.getProject(exampleId);

        assertEquals("123", result.id());
    }

    @Test
    void shouldThrowExceptionWhenProjectFound() {
        when(projectRepository.findById(exampleId))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.getProject(exampleId));

        assertTrue(exception.getMessage().contains("Not found project"));
    }

    @Test
    void shouldEditProjectPartially() {
        String username = "u1";
        String userId = "user-abc";

        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        Project existingProject = new Project();
        existingProject.setId(exampleId);
        existingProject.setTitle("old title");
        existingProject.setUsers(List.of(user));
        existingProject.setTasks(new ArrayList<>());

        when(projectRepository.findById(exampleId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        ProjectPatchRequestDto changedProject = new ProjectPatchRequestDto("new title", null);

        ProjectResponseDto result = projectService.editPartialProject(exampleId, changedProject, username);

        assertEquals("new title", result.title());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void shouldDeleteProject() {
        Project project = new Project();
        project.setId(exampleId);
        project.setUsers(new ArrayList<>());

        SecurityContext securityContext = mock(SecurityContext.class);


        SecurityContextHolder.setContext(securityContext);

        when(projectRepository.findById(exampleId)).thenReturn(Optional.of(project));

        assertDoesNotThrow(() -> projectService.deleteProject(exampleId));

        verify(projectRepository).delete(project);

        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldThrowExceptionWhenProjectToDeleteNotExist() {
        when(projectRepository.findById(exampleId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> projectService.deleteProject(exampleId));

        assertTrue(exception.getMessage().contains("Not found project"));

        verify(projectRepository, never()).delete(any());
    }
}
