package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
    private BookStatus status;
    @JsonIncludeProperties({"id", "username"})
    private User user;
    private Book book;
    private List<Tag> tags = new ArrayList<>();

    public static UserBook toEntity(UserBookDto userBookDto) {
        if (userBookDto == null) {
            return null;
        }
        UserBook userBook = new UserBook();
        userBook.setId(userBookDto.getId());
        userBook.setBook(userBookDto.getBook());
        userBook.setUser(userBookDto.getUser());
        userBook.setTags(userBookDto.getTags());
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
                .tags(userBook.getTags())
                .location(userBook.getLocation())
                .status(userBook.getStatus())
                .build();
    }
}
