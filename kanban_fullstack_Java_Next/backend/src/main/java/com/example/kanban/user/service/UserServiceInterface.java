package com.example.kanban.user.service;

import com.example.kanban.user.dto.LoginRequestDto;
import com.example.kanban.user.dto.RegisterRequestDto;
import com.example.kanban.user.dto.UserResponseDto;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserServiceInterface {
    UserResponseDto register(final RegisterRequestDto requestDto);

    String loginAndReturnToken(final LoginRequestDto requestDto);

    User getUserById(final String id);

    String getUserIdFromUsername(final String username);

    void changeUserRole(final String userId, final Role newRole);

    @Transactional(readOnly = true)
    List<UserResponseDto> getUsers();

    @Transactional(readOnly = true)
    UserResponseDto getMeFromToken(String token);
}
