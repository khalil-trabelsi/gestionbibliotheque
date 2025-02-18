package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}
