package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.dto.auth.AuthRequestDto;
import com.isima.gestionbibliotheque.dto.auth.AuthResponseDto;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("hello")
    public String hello() {
        return "Hello !";
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User createdUser = this.userService.register(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping(path="/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        String accessToken = this.userService.login(authRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }
}


