package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.controller.api.UserApiDocs;
import com.isima.gestionbibliotheque.dto.UpdateUserDto;
import com.isima.gestionbibliotheque.dto.UserDto;
import com.isima.gestionbibliotheque.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController implements UserApiDocs {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDto> getUserByUsername(String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserDto request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }


}
