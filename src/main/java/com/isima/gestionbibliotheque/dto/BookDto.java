package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.isima.gestionbibliotheque.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String subtitle;
    private String publishDate;
    private List<Publisher> publishers;
    private String description;
    private String coverImageUrl;
    private Date createdAt;
    private String numberOfPages;
    private List<Author> authors;
    @JsonIncludeProperties({"id", "status", "location", "rating", "tags", "user"})
    private List<UserBook> userBooks = new ArrayList<>();
    private CoverImage coverImage;
    @JsonIgnoreProperties({"book", "user"})
    private List<BookFeedback> bookFeedbacks = new ArrayList<>();
    private double rating;
    @JsonIgnore
    private List<Collection> collections = new ArrayList<>();

    public static BookDto fromEntity(Book book) {
        return book == null ? null :
                BookDto.builder()
                        .id(book.getId())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .subtitle(book.getSubtitle())
                        .publishDate(book.getPublishDate())
                        .publishers(book.getPublishers())
                        .description(book.getDescription())
                        .coverImageUrl(book.getCoverImageUrl())
                        .createdAt(book.getCreatedAt())
                        .authors(book.getAuthors())
                        .userBooks(book.getUserBooks())
                        .numberOfPages(book.getNumberOfPages())
                        .coverImage(book.getCoverImage())
                        .bookFeedbacks(book.getBookFeedback())
                        .rating(book.getRate())
                        .collections(book.getCollections())
                        .build();
    }

    public static Book toEntity(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setTitle(bookDto.getSubtitle());
        book.setPublishDate(bookDto.getPublishDate());
        book.setPublishers(bookDto.getPublishers());
        book.setDescription(bookDto.getDescription());
        book.setCreatedAt(bookDto.getCreatedAt());
        book.setCoverImageUrl(bookDto.getCoverImageUrl());
        book.setAuthors(bookDto.getAuthors());
        book.setUserBooks(bookDto.getUserBooks());
        book.setCoverImage(bookDto.getCoverImage());
        book.setNumberOfPages(bookDto.getNumberOfPages());
        book.setCollections(bookDto.getCollections());


        return book;

    }
}
