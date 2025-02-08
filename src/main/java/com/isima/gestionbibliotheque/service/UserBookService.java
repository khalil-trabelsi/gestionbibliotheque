package com.isima.gestionbibliotheque.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.UserBook;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserBookService {
    public List<UserBook> getAllBooksByUserId(Long userId);
    public UserBook addBook(String isbn, Long userId, String status, String location, int rating) throws JsonProcessingException;
    public void deleteBook(Long bookId);

}
