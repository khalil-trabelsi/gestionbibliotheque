package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.AuthRequestDto;
import com.isima.gestionbibliotheque.model.User;

public interface UserService {
    public User save(User user);
    public String login(AuthRequestDto authRequestDto);
    public User getUserById(Long id);
    public User getUserByUsername(String username);


}
