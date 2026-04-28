package com.example.kanban.auth.controller;

import com.example.kanban.exception.UnauthorizedException;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.LoginResponseDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

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

    UserResponseDto userResponseDto = new UserResponseDto("1", Role.USER, "user");

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequestDto request = new RegisterRequestDto("user", "user");

        when(userService.register(request)).thenReturn(userResponseDto);

        UserResponseDto result = userService.register(request);

        assertNotNull(result);
        assertEquals("user", result.username());
        verify(userService, times(1)).register(any());
    }

    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto("user", "user");

        String mockToken = "super-secret-jwt--token";

        LoginResponseDto loginUser = new LoginResponseDto(mockToken, "1", Role.USER, "user");
        when(userService.loginAndReturnUserWithToken(any())).thenReturn(loginUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", mockToken))
                .andExpect(cookie().httpOnly("token", true))
                .andExpect(cookie().secure("token", true))
                .andExpect(cookie().maxAge("token", 3600)) // 60 * 60
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void return401WhenUserNotFound() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto("unnown", "nevermind");

        when(userService.loginAndReturnUserWithToken(any(LoginRequestDto.class)))
                .thenThrow(new UnauthorizedException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/auth/register", "/auth/login"})
    void return400WhenInputDataEmpty(String url) throws Exception {

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }
}


