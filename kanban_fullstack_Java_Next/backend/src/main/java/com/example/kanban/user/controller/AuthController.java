package com.example.kanban.user.controller;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRequestDto request) {
        userService.register(request);
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody LoginRequestDto request) {
        return userService.login(request);
    }
}
