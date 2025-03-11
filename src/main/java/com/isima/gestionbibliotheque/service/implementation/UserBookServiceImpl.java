package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserBookServiceImpl implements UserBookService {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;


    @Autowired
    public UserBookServiceImpl(
            UserBookRepository userBookRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            TagRepository tagRepository
            ) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }
    @Override
    public List<UserBook> getAllBooksByUserId(Long userId) {
        // check if user already exists; else throw an exception
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find user with Id: %d", userId)));
        return userBookRepository.findAllByUserId(userId);
    }
    @Override
    public UserBook getUserBookById(Long userBookId) {
        return userBookRepository.findById(userBookId).orElseThrow(() -> new EntityNotFoundException("Cannot find UserBook with Id: "+userBookId, ErrorCode.USER_BOOK_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserBook createUserBook(Long bookId, String username) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with Id %d", bookId))
        );
        User user = userRepository.findUserByUsername(username);
        // if a link between a user and a book already exists then we will not create a new link
        UserBook userBook  = userBookRepository.findByBookIdAndUserId(bookId, user.getId());
        if (userBook != null) {
            return userBook;
        }

        // otherwise create a new user book
        userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setStatus(BookStatus.AVAILABLE);

        return userBookRepository.save(userBook);
    }

    @Transactional
    public void deleteUserBook(Long userBookId) {
        UserBook userBook = userBookRepository.findById(userBookId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find UserBook with ID " + userBookId)
        );
        if (userBook.getTags() != null) {
            for (Tag tag : userBook.getTags()) {
                tagRepository.deleteById(tag.getId());
            }
        }

        userBookRepository.delete(userBook);
    }




}
