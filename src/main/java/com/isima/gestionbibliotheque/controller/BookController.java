package com.isima.gestionbibliotheque.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/books")
public class BookController {

     private final BookService bookService;

     public BookController(BookService bookService) {
         this.bookService = bookService;
     }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
         return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }


    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> findBook(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String publisherName
    ) throws JsonProcessingException {
        return ResponseEntity.ok(bookService.findBooks(isbn, title, authorName, publisherName));
    }

}
