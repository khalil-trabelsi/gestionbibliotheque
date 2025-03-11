package com.isima.gestionbibliotheque.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> findBooks(String isbn, String title, String authorName, String publisherName) throws JsonProcessingException;
    List<BookDto> getAllBooks();

    BookDto getBookById(Long bookId);
}
