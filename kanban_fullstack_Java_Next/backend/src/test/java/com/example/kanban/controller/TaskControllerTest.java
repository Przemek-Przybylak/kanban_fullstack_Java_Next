package com.example.kanban.controller;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.exception.ForbiddenException;
import com.example.kanban.exception.IllegalRoleChangeException;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.TaskService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "test-user", roles = {"USER", "ADMIN"})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    ArgumentCaptor<TaskPatchRequestDto> taskDtoCaptor = ArgumentCaptor.forClass(TaskPatchRequestDto.class);

    @Test
    void shouldGetAllTasks() throws Exception {
        List<TaskResponseDto> tasks = List.of(
                new TaskResponseDto("1", "Task 1", "description 1", null, null, null, null, null, null),
                new TaskResponseDto("2", "Task 2", "description 2", null, null, null, null, null, null)
        );

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("test-user").roles("USER"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void shouldUpdateTaskPartially() throws Exception {
        String taskId = "123";

        when(taskService.editPartialTask(anyString(), any(), anyString()))
                .thenReturn(new TaskResponseDto(taskId, "New task", "Description", null, null, null, null, null, null));

        mockMvc.perform(patch("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New task\"}")
                        .with(user("test-user").roles("USER"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(taskService).editPartialTask(eq("123"), taskDtoCaptor.capture(), eq("test-user"));
        TaskPatchRequestDto captured = taskDtoCaptor.getValue();

        assertNull(captured.description());
        assertEquals("New task", captured.title());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        String taskId = "123";

        when(taskService.editTask(anyString(), any(), anyString()))
                .thenReturn(new TaskResponseDto(taskId, "New task", "Description", null, null, null, null, null, null));

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "New task",
                                    "description": "Some description",
                                    "status": "TODO"
                                }
                                """)
                        .with(user("test-user").roles("USER"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenPutTaskTitleIsBlank() throws Exception {
        String taskId = "123";
        TaskRequestDto request = new TaskRequestDto("", "description", "todo", null, null, null, null);

        when(taskService.editTask(eq(taskId), any(), anyString()))
                .thenThrow(new IllegalRoleChangeException("Title cannot be empty"));

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        String taskId = "123";

        mockMvc.perform(delete("/tasks/{id}", taskId)
                        .with(user("test-user").roles("USER", "ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(eq(taskId), eq("test-user"));
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingTask() throws Exception {
        String taskId = "999";

        doThrow(new NotFoundException("task", "id: " + taskId))
                .when(taskService).deleteTask(eq(taskId), anyString());

        mockMvc.perform(delete("/tasks/{id}", taskId)
                        .with(user("test-user"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("taskSecurityEndpoints")
    void shouldReturn403ForTaskEndpoints(MockHttpServletRequestBuilder methodBuilder) throws Exception {
        doThrow(new ForbiddenException("task"))
                .when(taskService).editTask(anyString(), any(), anyString());

        doThrow(new ForbiddenException("task"))
                .when(taskService).editPartialTask(anyString(), any(), anyString());

        doThrow(new ForbiddenException("task"))
                .when(taskService).deleteTask(anyString(), anyString());

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
    @MethodSource("taskSecurityEndpoints")
    void return404WhenUpdatedTaskNoExist(MockHttpServletRequestBuilder methodBuilder) throws Exception {
        String taskId = "123";
        String fullPath = "/tasks/" + taskId;
        TaskRequestDto request = new TaskRequestDto("new title", "description", "todo", null, null, null, "");


        doThrow(new NotFoundException("task", "id: " + taskId))
                .when(taskService).editTask(eq(taskId), any(), any());

        doThrow(new NotFoundException("task", "id: " + taskId))
                .when(taskService).editPartialTask(eq(taskId), any(), any());

        doThrow(new NotFoundException("task", "id: " + taskId))
                .when(taskService).deleteTask(eq(taskId), any());


        mockMvc.perform(methodBuilder
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "New task",
                                    "description": "Some description",
                                    "status": "TODO"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    static Stream<MockHttpServletRequestBuilder> taskSecurityEndpoints() {
        return Stream.of(
                put("/tasks/123"),
                patch("/tasks/123"),
                delete("/tasks/123")
        );
    }
}
