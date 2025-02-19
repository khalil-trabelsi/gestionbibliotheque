package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);


}
