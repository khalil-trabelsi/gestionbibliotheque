package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByIsbn(String isbn);

    List<Book> findAllByTitleOrAuthorsNameOrPublishersName(String title, String authorName, String publisherName);

}
