package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.auth.AuthRequestDto;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.User;

public interface UserService {
    public User register(UserRegistrationDto user);
    public String login(AuthRequestDto authRequestDto);
    public User getUserById(Long id);
    public User getUserByUsername(String username);


}
