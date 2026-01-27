package com.example.kanban.user.service;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserResponseDto register(final RegisterRequestDto requestDto) {
        Optional<User> isUsernameUse = userRepository.findByUsername(requestDto.
                username());
        if (isUsernameUse.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already used");
        }

        var user = new User();
        user.setRole(Role.USER);
        user.setUsername(requestDto.username());
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        userRepository.save(user);

        return new UserResponseDto(user.getId(), user.getRole(), user.getUsername());
    }

    public String loginAndReturnToken(final LoginRequestDto requestDto) {
        final var user = userRepository.findByUsername(requestDto.username())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid password"
            );
        }

        return jwtUtil.generateToken(user.getUsername());
    }


    @Transactional
    public User getUserById(String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public String getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id for " + username + " not found"));
    }

    @Transactional
    public void changeUserRole(String userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getUsername().equals("admin") && newRole != Role.ADMIN) {
            throw new RuntimeException("Nie można zmienić roli głównemu administratorowi!");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }

    public List<UserResponseDto> getUsers() {

        return userRepository.findAll().stream().map(user -> new UserResponseDto(user.getId(), user.getRole(), user.getUsername()))
                .toList();
    }

    public UserResponseDto getMeFromToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return new UserResponseDto(
                user.getId(),
                user.getRole(),
                user.getUsername()
        );
    }
}
