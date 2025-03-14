package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.BookFeedbackDto;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.UpdateFeedbackRequest;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.BookFeedback;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.BookFeedbackRepository;
import com.isima.gestionbibliotheque.repository.BookRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.BookFeedbackService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookFeedbackServiceImpl implements BookFeedbackService {

    private final BookFeedbackRepository bookFeedbackRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    @Override
    public BookFeedbackDto addBookFeedback(FeedbackRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Book book = bookRepository.findById(request.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with Id %d", request.getBookId()))
        );

        User currentUser = userRepository.findUserByUsername(authentication.getName());

        BookFeedback bookFeedback = new BookFeedback();
        bookFeedback.setBook(book);
        bookFeedback.setUser(currentUser);
        bookFeedback.setComment(request.getComment());
        bookFeedback.setRating(request.getRating());

        return BookFeedbackDto.fromEntity(bookFeedbackRepository.save(bookFeedback));

    }

    @Override
    public List<BookFeedbackDto> getAllBookFeedbackByBookId(Long bookId) {
        bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with Id %d", bookId))
        );
        return bookFeedbackRepository.findAllByBookId(bookId).stream().map(BookFeedbackDto::fromEntity).toList();
    }

    @Override
    public BookFeedbackDto findBookFeedbackById(Long feedbackId) {
        return null;
    }

    @Override
    public BookFeedbackDto findBookFeedbackByBookIdAndUserId(Long bookId, Long userId) {
        BookFeedback bookFeedback = bookFeedbackRepository.findByUserIdAndBookId(userId, bookId).orElseThrow(() -> new EntityNotFoundException("Cannot find Feedback"));
        return BookFeedbackDto.fromEntity(bookFeedback);
    }

    @Override
    public void deleteBookFeedback(Long feedbackId) {
        bookFeedbackRepository.deleteById(feedbackId);
    }

    @Override
    public BookFeedbackDto updateBookFeedback(Long feedbackId, UpdateFeedbackRequest request) {
        BookFeedback feedback = bookFeedbackRepository.findById(feedbackId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with Id %d", feedbackId))
        );

        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());


        return BookFeedbackDto.fromEntity(bookFeedbackRepository.save(feedback));

    }
}
