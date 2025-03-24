package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.UpdateUserDto;
import com.isima.gestionbibliotheque.dto.UserDto;
import com.isima.gestionbibliotheque.dto.auth.AuthRequest;
import com.isima.gestionbibliotheque.dto.auth.AuthResponse;
import com.isima.gestionbibliotheque.dto.auth.UserRegistrationDto;
import com.isima.gestionbibliotheque.model.CustomUserDetails;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.JwtService;
import com.isima.gestionbibliotheque.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository
    ) {
       this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::fromEntity).toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find user with #%s", id))
        );

        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userRepository.findUserByUsername(authentication.getName());
        User existingUser = userRepository.findById(userId).orElseThrow(
                () ->  new EntityNotFoundException("User not found with username: " + userId)
        );

        if (!currentUser.getId().equals(existingUser.getId())) {
            throw new AccessDeniedException("Forbidden.");
        }

        if (request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }

        if (request.getBirthDate() != null) {
            existingUser.setBirthDate(request.getBirthDate());
        }
        return UserDto.fromEntity(userRepository.save(existingUser));
    };

}
