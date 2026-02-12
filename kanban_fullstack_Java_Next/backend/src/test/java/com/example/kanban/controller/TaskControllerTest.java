package com.example.kanban.controller;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.TaskService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

        mockMvc.perform(patch("/tasks/123", taskId)
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

        mockMvc.perform(put("/tasks/123", taskId)
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
    void shouldDeleteTask() throws Exception {
        String taskId = "123";

        mockMvc.perform(delete("/tasks/{id}", taskId)
                .with(user("test-user").roles("USER", "ADMIN"))
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(eq(taskId), eq("test-user"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        String taskId = "999";

        doThrow(new NotFoundException("task", "id: " + taskId))
                .when(taskService).deleteTask(eq(taskId), anyString());

        mockMvc.perform(delete("/tasks/{id}", taskId)
                        .with(user("test-user"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
