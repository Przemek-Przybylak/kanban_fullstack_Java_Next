package com.example.kanban.auth.controller;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.LoginResponseDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        LoginResponseDto loginUser = userService.loginAndReturnUserWithToken(requestDto);

        org.springframework.http.ResponseCookie cookie = org.springframework.http.ResponseCookie.from("token", loginUser.token())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("None")
                .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        return new UserResponseDto(loginUser.id(), loginUser.role(), loginUser.username());
    }
}
