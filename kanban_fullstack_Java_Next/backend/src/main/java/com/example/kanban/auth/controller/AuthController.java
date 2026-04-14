package com.example.kanban.auth.controller;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/register", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String token = userService.loginAndReturnToken(requestDto);

        org.springframework.http.ResponseCookie cookie = org.springframework.http.ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("None")
                .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        final var user = userRepository.findByUsername(requestDto.username()).orElseThrow();
        return new UserResponseDto(user.getId(), user.getRole(), user.getUsername());
    }
}
