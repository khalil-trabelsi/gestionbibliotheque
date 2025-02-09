package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.dto.auth.AuthRequestDto;
import com.isima.gestionbibliotheque.dto.auth.AuthResponseDto;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDto user, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error: bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            throw new BadRequestException("Invalid input data", ErrorCode.USER_BOOK_NOT_VALID,errors);
        }
        User createdUser = this.userService.register(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping(path="/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        String accessToken = this.userService.login(authRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }
}


