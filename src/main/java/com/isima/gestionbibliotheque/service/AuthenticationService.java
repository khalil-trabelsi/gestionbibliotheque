package com.isima.gestionbibliotheque.service;


import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.ChangePasswordRequest;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    AuthResponse register(UserRegistrationDto user, HttpServletResponse response);
    AuthResponse login(AuthRequest authRequest, HttpServletResponse response);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void changePassword(Long userId, ChangePasswordRequest request);
}
