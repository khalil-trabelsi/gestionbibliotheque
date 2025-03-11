package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.BookFeedback;
import com.isima.gestionbibliotheque.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookFeedbackDto {
    private Long id;
    private double rating;
    private String comment;
    private Book book;
    private User user;

    public static BookFeedbackDto fromEntity(BookFeedback feedback) {
        return BookFeedbackDto
                .builder()
                .id(feedback.getId())
                .book(feedback.getBook())
                .user(feedback.getUser())
                .comment(feedback.getComment())
                .rating(feedback.getRating())
                .build();
    }
}
