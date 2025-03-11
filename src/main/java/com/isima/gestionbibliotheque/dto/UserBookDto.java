package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.*;
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
    private double rating;
    private String comment;
    private BookStatus status;
    private User user;
    private Book book;
    private List<Tag> tags = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();

    public static UserBook toEntity(UserBookDto userBookDto) {
        if (userBookDto == null) {
            return null;
        }
        UserBook userBook = new UserBook();
        userBook.setId(userBookDto.getId());
        userBook.setBook(userBookDto.getBook());
        userBook.setComment(userBookDto.getComment());
        userBook.setUser(userBookDto.getUser());
        userBook.setTags(userBookDto.getTags());
        userBook.setRating(userBookDto.getRating());
        userBook.setLocation(userBookDto.getLocation());
        userBook.setStatus(userBookDto.getStatus());
        userBook.setCollections(userBookDto.getCollections());

        return userBook;
    }

    public static UserBookDto fromEntity(UserBook userBook) {
        return userBook == null
                ? null
                : UserBookDto.builder()
                .id(userBook.getId())
                .book(userBook.getBook())
                .user(userBook.getUser())
                .tags(userBook.getTags())
                .rating(userBook.getRating())
                .location(userBook.getLocation())
                .status(userBook.getStatus())
                .comment(userBook.getComment())
                .collections(userBook.getCollections())
                .build();
    }
}
