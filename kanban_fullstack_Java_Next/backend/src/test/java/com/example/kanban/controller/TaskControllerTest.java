package com.example.kanban.controller;

import com.example.kanban.DTO.TaskPatchRequestDto;
import com.example.kanban.DTO.TaskResponseDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
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
    @WithMockUser(username = "test-user", roles = {"USER", "ADMIN"})
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
    @WithMockUser(username = "test-user", roles = {"USER", "ADMIN"})
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
}
