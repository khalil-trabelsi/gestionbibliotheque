package com.isima.gestionbibliotheque.service;

public interface JwtService {
    public String generateToken(String username);
    public boolean isTokenValid(String token, String username);

    public String extractUsername(String token);
}
