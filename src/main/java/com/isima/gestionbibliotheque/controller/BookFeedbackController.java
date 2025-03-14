package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.controller.api.BookFeedbackApiDocs;
import com.isima.gestionbibliotheque.dto.BookFeedbackDto;

import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.UpdateFeedbackRequest;
import com.isima.gestionbibliotheque.service.BookFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookFeedbackController implements BookFeedbackApiDocs {

    private final BookFeedbackService bookFeedbackService;

    @Override
    public ResponseEntity<List<BookFeedbackDto>> getAllBookFeedbackByBookId(@PathVariable Long bookId) {

        return ResponseEntity.ok(bookFeedbackService.getAllBookFeedbackByBookId(bookId));
    }

    @Override
    public ResponseEntity<BookFeedbackDto> getBookFeedbackById(@PathVariable Long feedbackId) {
        return ResponseEntity.ok(bookFeedbackService.findBookFeedbackById(feedbackId));
    }

    @Override
    public ResponseEntity<BookFeedbackDto> getBookFeedbackByBookIdAndUserId(
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "userId") Long userId
            ) {
        return ResponseEntity.ok(bookFeedbackService.findBookFeedbackByBookIdAndUserId(bookId, userId));
    }

    @Override
    public ResponseEntity<BookFeedbackDto> createBookFeedback(@RequestBody FeedbackRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookFeedbackService.addBookFeedback(request));
    }


    @Override
    public ResponseEntity<BookFeedbackDto> updateBookFeedback(@PathVariable Long feedbackId, @RequestBody UpdateFeedbackRequest request) {
        return ResponseEntity.ok(bookFeedbackService.updateBookFeedback(feedbackId, request));
    }
    @Override
    public void deleteBookFeedback(@PathVariable Long feedbackId) {
        bookFeedbackService.deleteBookFeedback(feedbackId);
    }


}
