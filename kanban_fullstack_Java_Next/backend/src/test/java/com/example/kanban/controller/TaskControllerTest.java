package com.example.kanban.controller;

import com.example.kanban.DTO.TaskResponseDto;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.service.TaskService;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @Test
    @WithMockUser(username = "test-user", roles = {"USER", "ADMIN"})
    void shouldUpdateTask() throws Exception {
        String taskId = "123";

        when(taskService.editPartialTask(anyString(), any(), anyString()))
                .thenReturn(new TaskResponseDto(taskId, "Nowe zadanie", "Opis", null, null, null, null, null, null));

        mockMvc.perform(patch("/tasks/123", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Nowe zadanie\"}")
                        .with(user("test-user").roles("USER"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
