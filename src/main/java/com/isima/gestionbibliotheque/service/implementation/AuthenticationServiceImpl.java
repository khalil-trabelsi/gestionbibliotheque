package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.ChangePasswordRequest;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.CustomUserDetails;
import com.isima.gestionbibliotheque.model.Token;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.CollectionRepository;
import com.isima.gestionbibliotheque.repository.TokenRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.AuthenticationService;
import com.isima.gestionbibliotheque.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final CollectionRepository collectionRepository;


    @Override
    public AuthResponse register(UserRegistrationDto userRegistrationDto, HttpServletResponse response) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setBirthDate(userRegistrationDto.getBirthDate());
        user.setCreatedAt(new Date());
        var savedUser = userRepository.save(user);

        var jwt = jwtService.generateToken(savedUser.getUsername());
        var refreshToken = jwtService.generateRefreshToken(savedUser.getUsername());
        saveUserToken(savedUser, jwt);

        Cookie jwtCookie = createAuthCookie("access-token", jwt);
        Cookie refreshTokenCookie = createAuthCookie("refresh-token", refreshToken);


        response.addCookie(jwtCookie);
        response.addCookie(refreshTokenCookie);
        // By default, each user must have 5 collections
        Map<String, String> defaultCollections = Map.of(
                "j'ai", "Les livres que je possède",
                "whishlist", "Les livres que je souhaite acquérir",
                "j'ai lu", "Les livres que j'ai déjà lus",
                "je lis", "Les livres que je suis en train de lire",
                "j'aime", "Mes livres préférés"
        );

        defaultCollections.forEach((name, description) -> {
            Collection collection = new Collection();
            collection.setName(name);
            collection.setDescription(description);
            collection.setUser(user);
            collection.setShareable(false);
            collectionRepository.save(collection);
        });

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                authRequest.getPassword()));
        var user = userRepository.findUserByUsername(((CustomUserDetails) auth.getPrincipal()).getUsername());

        var jwt = jwtService.generateToken(user.getUsername());
        var refreshToken = jwtService.generateRefreshToken(user.getUsername());
        revokeAllTokens(user);
        saveUserToken(user, jwt);

        Cookie jwtCookie = createAuthCookie("access-token", jwt);
        Cookie refreshTokenCookie = createAuthCookie("refresh-token", refreshToken);


        response.addCookie(jwtCookie);
        response.addCookie(refreshTokenCookie);
        return AuthResponse.builder().accessToken(jwt).refreshToken(refreshToken).userId(user.getId()).build();
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

    @Transactional
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found with Id: "+userId)
        );


        if (passwordEncoder.matches(user.getPassword(), request.getOldPassword())) {
            throw new BadRequestException("L'ancien mot de passe est incorrect");
        }
        String hashedNewPassword = passwordEncoder.encode(request.getOldPassword());

        user.setPassword(hashedNewPassword);
        userRepository.save(user);
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


    private Cookie createAuthCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(true);
        cookie.setMaxAge(4200);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
//        jwtCookie.setHttpOnly(true);

        return cookie;
    }



}
