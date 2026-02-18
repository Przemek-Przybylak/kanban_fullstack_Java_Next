package com.example.kanban.controller;

import com.example.kanban.DTO.*;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.ProjectService;
import com.example.kanban.service.TaskService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private TaskService taskService;

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

    @Test
    void shouldAddTask() throws Exception {
        shortProjectDto project = new shortProjectDto("1.1", "title 1.1");
        String projectId = "1.1";
        TaskRequestDto task = new TaskRequestDto("title 1", "description 1", "TODO", null, null, "admin", "1.1");
        TaskResponseDto addedTask = new TaskResponseDto("1.1", "title 2", "description 2", "TODO", null, null, null, project, "admin");

        when(projectService.addTask(eq(projectId), any(TaskRequestDto.class), eq("admin")))
                .thenReturn(addedTask);

        mockMvc.perform(post("/projects/{id}/tasks", projectId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedTask)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/1.1")))
                .andExpect(jsonPath("$.id").value("1.1"))
                .andExpect(jsonPath("$.title").value("title 2"));
    }

    @Test
    void shouldUpdateStatus() throws Exception {
        String status = "done";
        String taskId = "1";
        TaskStatusRequest request = new TaskStatusRequest(status);

        mockMvc.perform(patch("/projects/{taskId}/tasks", taskId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(taskService).updateStatus(eq(taskId), eq(status));
    }

    @Test
    void shouldAddProject() throws Exception {
        String username = "admin";
        ProjectRequestDto project = new ProjectRequestDto("title 1", "description 1");

        ProjectResponseDto savedProject = new ProjectResponseDto("100", "title 1", "description 1", null, null, null, null);

        when(projectService.addProject(any(ProjectRequestDto.class), eq(username)))
                .thenReturn(savedProject);

        mockMvc.perform(post("/projects")
                        .with(csrf())
                        .with(user(username))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/projects/100")))
                .andExpect(jsonPath("$.id").value("100"))
                .andExpect(jsonPath("$.title").value("title 1"));
    }
}
