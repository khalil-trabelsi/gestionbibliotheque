package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findBookByIsbn(String isbn);
}
