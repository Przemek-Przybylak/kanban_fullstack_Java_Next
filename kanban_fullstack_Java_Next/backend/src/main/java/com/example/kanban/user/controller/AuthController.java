package com.example.kanban.user.controller;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

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
    public UserResponseDto register(@RequestBody RegisterRequestDto requestDto) {
       return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
    ) {
        String token = userService.loginAndReturnToken(requestDto);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        final var user = userRepository.findByUsername(requestDto.username())
                .orElseThrow();
        return new UserResponseDto(user.getId(), user.getRole(), user.getUsername());
    }

    @GetMapping("/me")
    public UserResponseDto me(HttpServletRequest request) {

        if (request.getCookies() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = Arrays.stream(request.getCookies())
                .filter(c -> "token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return userService.getMeFromToken(token);
    }
}
