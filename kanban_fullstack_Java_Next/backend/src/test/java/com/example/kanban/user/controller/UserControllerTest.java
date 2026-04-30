package com.example.kanban.user.controller;

import com.example.kanban.auth.controller.AuthController;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(List.of(userResponseDto1, userResponseDto2));

        List<UserResponseDto> result = userService.getUsers();

        assertEquals(2, result.size());
        assertEquals("user1", result.getFirst().username());
        assertEquals("user2", result.get(1).username());
    }


}


