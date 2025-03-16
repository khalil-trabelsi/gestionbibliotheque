package com.isima.gestionbibliotheque.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.controller.api.BookApiDocs;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.UserBookDto;
import com.isima.gestionbibliotheque.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController implements BookApiDocs {

     private final BookService bookService;

     public BookController(BookService bookService) {
         this.bookService = bookService;
     }

     @Override
     public ResponseEntity<List<BookDto>> getAllBooks() {
         return ResponseEntity.ok(bookService.getAllBooks());
    }

     @Override
     public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
     }

    @Override
    public ResponseEntity<List<UserBookDto>> getAllBooksByUserId(@PathVariable Long userId) {
         return ResponseEntity.ok(bookService.getAllBooksByUserId(userId));
    }


    @Override
    public ResponseEntity<List<BookDto>> findBook(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String publisherName
    ) throws JsonProcessingException {
        return ResponseEntity.ok(bookService.findBooks(isbn, title, authorName, publisherName));
    }

    @Override
    public void deleteBookFromLibrary(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "bookId") Long bookId
    ) {
         bookService.deleteBookFromLibrary(userId, bookId);
    }




}
