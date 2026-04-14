package com.example.kanban.user.controller;

import com.example.kanban.exception.UnauthorizedException;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.RoleUpdateRequest;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class LoginController {

    private final UserService userService;
    private final UserRepository userRepository;

    public LoginController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable String userId, @Valid @RequestBody RoleUpdateRequest roleUpdate) {
        userService.changeUserRole(userId, roleUpdate.role());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/me")
    public UserResponseDto me(HttpServletRequest request) {

        if (request.getCookies() == null) {
            throw new UnauthorizedException("username");
        }

        String token = Arrays.stream(request.getCookies())
                .filter(c -> "token".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new UnauthorizedException("username"));

        return userService.getMeFromToken(token);
    }
}
