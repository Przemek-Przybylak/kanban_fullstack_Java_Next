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

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public User register(RegisterRequestDto requestDto) {
        Optional<User> isUsernameUse = userRepository.findByUsername(requestDto.
                username());
        if (isUsernameUse.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already used");
        }

        User user = new User();
        user.setRole(Role.USER);
        user.setUsername(requestDto.username());
        user.setPassword(passwordEncoder.encode(requestDto.password()));

        return userRepository.save(user);
    }

    public UserResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.username())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid password"
            );
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new UserResponseDto(token, user.getId(), user.getRole(), user.getUsername());
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
}
