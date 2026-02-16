package com.example.kanban.controller;

import com.example.kanban.DTO.ProjectResponseDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.DTO.shortProjectDto;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.ProjectService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @Test
    void shouldGetAllProjects() throws Exception {
        List<ProjectResponseDto> projects = List.of(
                new ProjectResponseDto("1", "title 1", "description 1", null, null, null, null),
                new ProjectResponseDto("2", "title 2", "description 2", null, null, null, null)
        );

        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("title 1"))
                .andExpect(jsonPath("$[1].title").value("title 2"));
    }

    @Test
    void shouldGetProject() throws Exception {
        ProjectResponseDto project = new ProjectResponseDto("1", "title 1", "description 1", null, null, null, null);

        when(projectService.getProject(project.id())).thenReturn(project);

        mockMvc.perform(get("/projects/{id}", project.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value("title 1"));
    }

    @Test
    void shouldGetTasksByProject() throws Exception {
        shortProjectDto project = new shortProjectDto("1.1", "title 1.1");
        List<TaskResponseDto> tasks = List.of(
                new TaskResponseDto("1", "title 1", "description 1", "TODO", null, null, null, project, "admin")
        );

        when(projectService.getTasksByProject(project.id())).thenReturn(tasks);

        mockMvc.perform(get("/projects/{id}/tasks", project.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("title 1"));
    }
}
