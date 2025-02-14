package com.isima.gestionbibliotheque.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.model.UserBook;

import java.util.List;

public interface UserBookService {
    public List<UserBook> getAllBooksByUserId(Long userId);
    public UserBook getUserBookById(Long userBookId);
    public UserBook createUserBook(String isbn, Long userId, String status, String location, int rating) throws JsonProcessingException;
    public void deleteUserBook(Long userBookId);

}
