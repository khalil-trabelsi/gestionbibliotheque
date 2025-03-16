package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.OperationNotPermittedException;
import com.isima.gestionbibliotheque.dto.BookFeedbackDto;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.UpdateFeedbackRequest;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.BookFeedback;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.BookFeedbackRepository;
import com.isima.gestionbibliotheque.repository.BookRepository;
import com.isima.gestionbibliotheque.repository.CollectionRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.BookFeedbackService;
import com.isima.gestionbibliotheque.service.UserBookService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookFeedbackServiceImpl implements BookFeedbackService {

    private final BookFeedbackRepository bookFeedbackRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CollectionRepository collectionRepository;
    private final UserBookService userBookService;


    @Override
    @Transactional
    public BookFeedbackDto addBookFeedback(FeedbackRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findUserByUsername(authentication.getName());

        Book book = bookRepository.findById(request.getBookId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with Id %d", request.getBookId()))
        );

        // Check if the user has already given feedback for this book
        if (bookFeedbackRepository.findByUserIdAndBookId(currentUser.getId(), book.getId()).isPresent()) {
            throw new IllegalStateException("You have already given feedback for this book.");
        }

        // Find or create the collection and add the book
        String collectionName = String.format("Mes livres %d étoiles", request.getRating());
        Collection collection = findOrCreateCollection(collectionName, currentUser);

        collection.getBooks().add(book);
        collectionRepository.save(collection);

        // Associate the book to the current user
        userBookService.createUserBook(book.getId(), currentUser.getUsername());

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
    @Transactional
    public BookFeedbackDto updateBookFeedback(Long feedbackId, UpdateFeedbackRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findUserByUsername(authentication.getName());

        // Check if feedback exists
        BookFeedback feedback = bookFeedbackRepository.findById(feedbackId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book feedback with Id %d", feedbackId))
        );

        if (!feedback.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(String.format("You don't have permissions to modify feedback with ID %d", feedbackId));
        }

        // Find old collection
        String oldCollectionName = String.format("Mes livres %d étoiles", feedback.getRating());
        Collection oldCollection = Optional.ofNullable(collectionRepository.findByNameAndUserId(oldCollectionName, currentUser.getId()))
                .orElseThrow(() -> new IllegalStateException("Expected collection not found: " + oldCollectionName));

        if (feedback.getRating() != request.getRating()) {
            // remove book from old collection
            oldCollection.getBooks().remove(feedback.getBook());
            collectionRepository.save(oldCollection);

            // Find or create the new collection and add book
            String newCollectionName = String.format("Mes livres %d étoiles", request.getRating());
            Collection newCollection = findOrCreateCollection(newCollectionName, currentUser);
            newCollection.getBooks().add(feedback.getBook());
            collectionRepository.save(newCollection);
        }

        // Update feedback
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        return BookFeedbackDto.fromEntity(bookFeedbackRepository.save(feedback));
    }

    @Override
    @Transactional
    public void deleteBookFeedback(Long feedbackId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findUserByUsername(authentication.getName());

        BookFeedback feedback = bookFeedbackRepository.findById(feedbackId).orElseThrow();

        if (!feedback.getUser().getId().equals(currentUser.getId())) {
            throw new OperationNotPermittedException("You are not allowed to delete this feedback.");

        }

        String collectionName = String.format("Mes livres %d étoiles", feedback.getRating());
        Collection collection = collectionRepository.findByNameAndUserId(collectionName, currentUser.getId());

        if (collection != null) {
            collection.getBooks().remove(feedback.getBook());
            collectionRepository.save(collection);
        }


        bookFeedbackRepository.deleteById(feedbackId);
    }

    private Collection findOrCreateCollection(String name, User user) {
        Collection collection = collectionRepository.findByNameAndUserId(name, user.getId());

        if (collection != null) {
            return collection;
        }

        Collection newCollection = new Collection();
        newCollection.setName(name);
        newCollection.setUser(user);
        newCollection.setShareable(false);
        return newCollection;
    }

}
