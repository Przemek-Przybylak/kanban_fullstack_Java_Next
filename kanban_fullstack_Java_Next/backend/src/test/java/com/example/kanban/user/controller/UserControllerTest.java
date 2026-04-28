package com.example.kanban.user.controller;

import com.example.kanban.auth.controller.AuthController;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

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

    UserResponseDto userResponseDto = new UserResponseDto("1", Role.USER, "user");


}


