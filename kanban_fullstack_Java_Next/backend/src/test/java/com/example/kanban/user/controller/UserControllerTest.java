package com.example.kanban.user.controller;

import com.example.kanban.auth.controller.AuthController;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.model.RoleUpdateRequest;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    UserResponseDto userResponseDto1 = new UserResponseDto("1", Role.USER, "user1");
    UserResponseDto userResponseDto2 = new UserResponseDto("2", Role.USER, "user2");

    String userId = "123";
    RoleUpdateRequest newRole = new RoleUpdateRequest(Role.ADMIN);

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(userResponseDto1, userResponseDto2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUserRole() throws Exception {

        mockMvc.perform(patch("/users/{userId}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService).changeUserRole(userId, Role.ADMIN);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenUserNotFound() throws Exception {
        doThrow(new NotFoundException("this", "userId"))
                .when(userService).changeUserRole(eq(userId), any());

        mockMvc.perform(patch("/users/{userId}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}


