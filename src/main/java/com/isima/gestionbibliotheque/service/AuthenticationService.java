package com.isima.gestionbibliotheque.service;


import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    AuthResponse register(UserRegistrationDto user);
    AuthResponse login(AuthRequest authRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
