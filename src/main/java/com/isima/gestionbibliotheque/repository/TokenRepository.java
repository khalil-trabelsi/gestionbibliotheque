package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query(value = """ 
        SELECT t from Token t\s
        inner join User u on u.id = t.user.id  \s
        where t.user.id = :id and (t.expired = false or t.revoked = false)
    """
    )
    List<Token> findAllValidTokenByUserId(Long id);
}
