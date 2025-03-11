package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.BookFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookFeedbackRepository extends JpaRepository<BookFeedback, Long> {

    Optional<BookFeedback> findByUserIdAndBookId(Long userId, Long bookId);

    List<BookFeedback> findAllByBookId(Long bookId);
}
