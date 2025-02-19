package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.CustomUserDetails;
import com.isima.gestionbibliotheque.model.Token;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.TokenRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.AuthenticationService;
import com.isima.gestionbibliotheque.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;


    @Override
    public AuthResponse register(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setBirthDate(userRegistrationDto.getBirthDate());
        user.setCreatedAt(new Date());
        var savedUser = userRepository.save(user);

        var jwt = jwtService.generateToken(savedUser.getUsername());
        var refreshToken = jwtService.generateRefreshToken(savedUser.getUsername());
        saveUserToken(savedUser, jwt);
        return AuthResponse.builder().accessToken(jwt).refreshToken(refreshToken).build();
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                authRequest.getPassword()));
        var user = userRepository.findUserByUsername(((CustomUserDetails) auth.getPrincipal()).getUsername());

        var jwt = jwtService.generateToken(user.getUsername());
        var refreshToken = jwtService.generateRefreshToken(user.getUsername());
        revokeAllTokens(user);
        saveUserToken(user, jwt);
        return AuthResponse.builder().accessToken(jwt).refreshToken(refreshToken).build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken;
        String username;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findUserByUsername(username);
            if (jwtService.isTokenValid(refreshToken, username)) {
                var accessToken = jwtService.generateToken(username);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }

    }

    private void saveUserToken(User user, String accessToken) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setUser(user);
        token.setRevoked(false);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    private void revokeAllTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        for (Token token: validUserTokens) {
            token.setExpired(true);
            token.setRevoked(true);
        }
        tokenRepository.saveAll(validUserTokens);

    }

}
