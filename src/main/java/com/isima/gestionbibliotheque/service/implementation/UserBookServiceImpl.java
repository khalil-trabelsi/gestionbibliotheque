package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import com.isima.gestionbibliotheque.repository.BookRepository;
import com.isima.gestionbibliotheque.repository.UserBookRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserBookServiceImpl implements UserBookService {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Autowired
    public UserBookServiceImpl(
            UserBookRepository userBookRepository,
            BookRepository bookRepository,
            UserRepository userRepository
            ) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<UserBook> getAllBooksByUserId(Long userId) {
        return userBookRepository.findAllByUserId(userId);
    }


    @Override
    public UserBook addBook(Book book, Long userId, String status, String location, int rating) {
        Book existingBook = bookRepository.findBookByIsbn(book.getIsbn());
        if (existingBook == null) {
            book.setCreatedAt(new Date());
            existingBook = bookRepository.save(book);
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException(String.format("Cannot find user with Id %d", userId))
        );
        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setRating(rating);
        userBook.setLocation(location);
        userBook.setStatus(status);
        userBook.setBook(existingBook);


        return userBookRepository.save(userBook);
    }

    @Override
    public void deleteBook(Long bookId) {

    }
}
