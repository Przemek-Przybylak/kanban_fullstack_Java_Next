package com.example.kanban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseSecurityTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void performSecurityCheck(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder
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
}
