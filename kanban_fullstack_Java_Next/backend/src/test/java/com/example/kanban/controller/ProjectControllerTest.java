package com.example.kanban.controller;

import com.example.kanban.DTO.*;
import com.example.kanban.exception.ForbiddenException;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.ProjectService;
import com.example.kanban.service.TaskService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    String id = "2";
    String username = "admin";
    ProjectResponseDto savedProject = new ProjectResponseDto("2", "title 2", "description 2", null, null, null, null);
    ProjectResponseDto project = new ProjectResponseDto("1", "title 1", "description 1", null, null, null, null);
    shortProjectDto projectShort = new shortProjectDto("1.1", "title 1.1");
    @Test
    void shouldGetAllProjects() throws Exception {
        List<ProjectResponseDto> projects = List.of(
                project,
                savedProject
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

        List<TaskResponseDto> tasks = List.of(
                new TaskResponseDto("1", "title 1", "description 1", "TODO", null, null, null, projectShort, "admin")
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

        String projectId = "1.1";
        TaskRequestDto task = new TaskRequestDto("title 1", "description 1", "TODO", null, null, "admin", "1.1");
        TaskResponseDto addedTask = new TaskResponseDto("1.1", "title 2", "description 2", "TODO", null, null, null, projectShort, "admin");

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

    @Test
    void shouldUpdateProject() throws Exception {
        ProjectRequestDto addedProject = new ProjectRequestDto("title", "description");

        when(projectService.editProject(eq(id), any(ProjectRequestDto.class), eq(username)))
                .thenReturn(savedProject);

        mockMvc.perform(put("/projects/{id}", id)
                        .with(csrf())
                        .with(user(username))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"));
    }

    @Test
    void shouldUpdateProjectPartially() throws Exception {
        ProjectPatchRequestDto addedProject = new ProjectPatchRequestDto("", "description");

        when(projectService.editPartialProject(eq(id), any(ProjectPatchRequestDto.class), eq(username)))
                .thenReturn(savedProject);

        mockMvc.perform(patch("/projects/{id}", id)
                        .with(csrf())
                        .with(user(username))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        mockMvc.perform(delete("/projects/{id}", id)
                        .with(csrf())
                        .with(user(username)))
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).deleteProject(eq(id));

    }

    @ParameterizedTest
    @MethodSource("projectSecurityEndpoints")
    void shouldReturn403ForTasEndpoints(MockHttpServletRequestBuilder methodBuilder) throws Exception {
        doThrow(new ForbiddenException("project"))
                .when(projectService).addProject(any(), anyString());

        doThrow(new ForbiddenException("task"))
                .when(projectService).addTask(anyString(), any(), anyString());

        doThrow(new ForbiddenException("project"))
                .when(projectService).editProject(anyString(), any(), anyString());

        doThrow(new ForbiddenException("project"))
                .when(projectService).editPartialProject(anyString(), any(), anyString());

        doThrow(new ForbiddenException("project"))
                .when(projectService).deleteProject(anyString());

        mockMvc.perform(methodBuilder
                        .with(user("intruder").roles("Guest"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "title": "New task",
                        "description": "Some description",
                        "status": "TODO"
                    }
                    """))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @MethodSource("projectSecurityEndpoints")
    void return404WhenUpdatedProjectNoExist(MockHttpServletRequestBuilder methodBuilder) throws Exception {
        String projectId = "123";
        String fullPath = "/projects/" + projectId;
        ProjectRequestDto request = new ProjectRequestDto("new title", "description");


        doThrow(new NotFoundException("project", "id" + projectId))
                .when(projectService).editProject(eq(projectId), any(), any());

        doThrow(new NotFoundException("project", "id" + projectId))
                .when(projectService).editPartialProject(eq(projectId), any(), any());

        doThrow(new NotFoundException("project", "id" + projectId))
                .when(projectService).deleteProject(eq(projectId));


        mockMvc.perform(methodBuilder
                .with(user("admin").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "New project",
                            "description": "Some description",
                            "status": "TODO"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    static Stream<MockHttpServletRequestBuilder> projectSecurityEndpoints() {
        return Stream.of(
                post("/projects"),
                post("/projects/{id}/tasks", 1),
                put("/projects/123"),
                patch("/projects/123"),
                delete("/projects/123")
        );
    }
}
