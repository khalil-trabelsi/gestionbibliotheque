package com.isima.gestionbibliotheque.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.model.UserBook;

import java.util.List;

public interface UserBookService {
    List<UserBook> getAllBooksByUserId(Long userId);
    UserBook getUserBookById(Long userBookId);
    UserBook createUserBook(Long bookId, String username);
    void deleteUserBook(Long userBookId);

    UserBook getUserBookByUserIdAndBookId(Long userId, Long bookId);

}
