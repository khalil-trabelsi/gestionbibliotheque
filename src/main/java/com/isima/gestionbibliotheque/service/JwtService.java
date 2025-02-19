package com.isima.gestionbibliotheque.service;

public interface JwtService {
    String generateToken(String username);
    String generateRefreshToken(String username);
    boolean isTokenValid(String token, String username);
    String extractUsername(String token);
}
