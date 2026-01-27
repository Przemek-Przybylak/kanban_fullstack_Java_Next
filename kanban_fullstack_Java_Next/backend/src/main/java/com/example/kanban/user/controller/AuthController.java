package com.example.kanban.user.controller;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.RoleUpdateRequest;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

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
    public UserResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String token = userService.loginAndReturnToken(requestDto);


        org.springframework.http.ResponseCookie cookie = org.springframework.http.ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        final var user = userRepository.findByUsername(requestDto.username()).orElseThrow();
        return new UserResponseDto(user.getId(), user.getRole(), user.getUsername());
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable String userId, @RequestBody @Valid RoleUpdateRequest roleUpdate) {
        userService.changeUserRole(userId, roleUpdate.role());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
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
