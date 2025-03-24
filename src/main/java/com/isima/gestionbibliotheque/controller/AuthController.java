package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.ChangePasswordRequest;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.Token;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.TokenRepository;
import com.isima.gestionbibliotheque.service.AuthenticationService;
import com.isima.gestionbibliotheque.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Authentication")
public class AuthController {
    private final AuthenticationService authenticationService;
    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody UserRegistrationDto request,
            BindingResult bindingResult,
            HttpServletResponse response) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError error: bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
            throw new BadRequestException("Invalid input data", null,errors);
        }
        AuthResponse authResponse = this.authenticationService.register(request, response);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping(path="/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        return ResponseEntity.ok(this.authenticationService.login(authRequest, response));
    }

    @PostMapping(path="/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PutMapping(path="/users/{userId}/change-password")
    public void changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest request) {
        this.authenticationService.changePassword(userId, request);
    }




}


