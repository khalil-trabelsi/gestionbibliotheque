package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.UserBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date publicationYear;
    private String publisher;
    private String description;
    private String coverImageUrl;
    private Date createdAt;
    private String author;
    private List<UserBook> userBooks = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();

    public static BookDto fromEntity(Book book) {
        return book == null ? null :
                BookDto.builder()
                        .id(book.getId())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .subtitle(book.getSubtitle())
                        .publicationYear(book.getPublicationYear())
                        .publisher(book.getPublisher())
                        .description(book.getDescription())
                        .coverImageUrl(book.getCoverImageUrl())
                        .createdAt(book.getCreatedAt())
                        .author(book.getAuthor())
                        .userBooks(book.getUserBooks())
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
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setPublisher(bookDto.getPublisher());
        book.setDescription(bookDto.getDescription());
        book.setCollections(bookDto.getCollections());
        book.setCreatedAt(bookDto.getCreatedAt());
        book.setCoverImageUrl(bookDto.getCoverImageUrl());
        book.setAuthor(bookDto.getAuthor());
        book.setUserBooks(bookDto.getUserBooks());

        return book;

    }
}
