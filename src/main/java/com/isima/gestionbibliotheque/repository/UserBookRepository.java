package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findAllByUserId(Long user_id);
}
