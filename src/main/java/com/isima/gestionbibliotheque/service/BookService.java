package com.isima.gestionbibliotheque.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.UserBookDto;

import java.util.List;

public interface BookService {
    List<BookDto> findBooks(String isbn, String title, String authorName, String publisherName) throws JsonProcessingException;
    List<BookDto> getAllBooks();

    BookDto getBookById(Long bookId);

    List<UserBookDto> getAllBooksByUserId(Long bookId);

    void deleteBookFromLibrary(Long bookId, Long userId);
}
