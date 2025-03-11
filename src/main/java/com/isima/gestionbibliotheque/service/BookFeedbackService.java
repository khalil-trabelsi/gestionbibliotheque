package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.BookFeedbackDto;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.UpdateFeedbackRequest;

import java.util.List;

public interface BookFeedbackService {
    BookFeedbackDto addBookFeedback(FeedbackRequest request);

    List<BookFeedbackDto> getAllBookFeedbackByBookId(Long bookId);

    BookFeedbackDto findBookFeedbackById(Long feedbackId);

    BookFeedbackDto findBookFeedbackByBookIdAndUserId(Long bookId, Long userId);


    void deleteBookFeedback(Long feedbackId);

    BookFeedbackDto updateBookFeedback(Long feedbackId, UpdateFeedbackRequest request);




}
