package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.UpdateUserDto;
import com.isima.gestionbibliotheque.dto.UserDto;
import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);
    UserDto getUserByUsername(String username);
    UserDto updateUser(Long userId, UpdateUserDto request);

    List<UserDto> getAllUsers();


}
