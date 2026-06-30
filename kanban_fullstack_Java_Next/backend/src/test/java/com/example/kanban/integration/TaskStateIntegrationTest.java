package com.example.kanban.integration;

import com.example.kanban.DTO.TaskStatusRequest;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskStateIntegrationTest extends BaseTestConfigWithTask {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void shouldChangeTaskStatus() throws Exception {
        TaskStatusRequest statusRequest = new TaskStatusRequest("in_progress");

        mockMvc.perform(patch("/projects/{taskId}/tasks", taskId)
                        .with(jwt().jwt(jwt -> jwt.claim("sub", "admin"))
                                .authorities(
                                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ADMIN")
                                ))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest))
                        .with(csrf()))
                .andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(taskId).orElseThrow();
        assertEquals("in_progress", updatedTask.getStatus());
    }
}