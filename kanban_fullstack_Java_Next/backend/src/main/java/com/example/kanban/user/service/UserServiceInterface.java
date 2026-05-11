package com.example.kanban.user.service;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.LoginResponseDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserServiceInterface {
    UserResponseDto register(final RegisterRequestDto requestDto);

    LoginResponseDto loginAndReturnUserWithToken(final LoginRequestDto requestDto);

    String getUserIdFromUsername(final String username);

    void changeUserRole(final String userId, final Role newRole);

    List<UserResponseDto> getUsers();

    UserResponseDto getMeFromToken(String token);
}
