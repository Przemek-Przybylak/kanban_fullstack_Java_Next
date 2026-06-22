package com.example.kanban.integration;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectSecurityIntegrationTest extends BaseTestConfig {

    @Test
    @WithMockUser(username = "przemek")
    public void shouldAllowAccessForPrzemek() throws Exception {
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nonPrzemek")
    public void shouldBlockAccessForNonPrzemek() throws Exception {
        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldBlockAccessForNotLoggedUser() throws Exception {
        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isUnauthorized());
    }
}