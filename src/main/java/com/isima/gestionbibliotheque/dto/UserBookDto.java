package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Tag;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBookDto {
    private Long id;
    private String location;
    private int rating;
    private String status;
    private User user;
    private Book book;
    private List<Tag> tag = new ArrayList<>();

    public static UserBook toEntity(UserBookDto userBookDto) {
        if (userBookDto == null) {
            return null;
        }
        UserBook userBook = new UserBook();
        userBook.setId(userBookDto.getId());
        userBook.setBook(userBookDto.getBook());
        userBook.setUser(userBookDto.getUser());
        userBook.setTag(userBookDto.getTag());
        userBook.setRating(userBookDto.getRating());
        userBook.setLocation(userBookDto.getLocation());
        userBook.setStatus(userBookDto.getStatus());

        return userBook;
    }

    public static UserBookDto fromEntity(UserBook userBook) {
        return userBook == null
                ? null
                : UserBookDto.builder()
                .id(userBook.getId())
                .book(userBook.getBook())
                .user(userBook.getUser())
                .tag(userBook.getTag())
                .rating(userBook.getRating())
                .location(userBook.getLocation())
                .status(userBook.getStatus())
                .build();
    }
}
