package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class CollectionDto {
    private Long id;
    private String name;
    private String description;

    @JsonProperty("isPublic")
    private boolean isPublic;

    private Date createdAt;

    @JsonIncludeProperties({"username", "id"})
    private User user;
    private List<Book> books = new ArrayList<>();

    public static Collection toEntity(CollectionDto collectionDto) {

        Collection collection = new Collection();

        collection.setId(collectionDto.getId());
        collection.setUser(collectionDto.getUser());
        collection.setName(collectionDto.getName());
        collection.setShareable(collectionDto.isPublic());
        collection.setDescription(collectionDto.getDescription());
        collection.setCreatedAt(collectionDto.getCreatedAt());
        collection.setBooks(collectionDto.getBooks());

        return collection;
    }

    public static CollectionDto fromEntity(Collection collection) {
        return collection == null ? null :
                CollectionDto
                        .builder()
                        .id(collection.getId())
                        .name(collection.getName())
                        .description(collection.getDescription())
                        .user(collection.getUser())
                        .books(collection.getBooks())
                        .createdAt(collection.getCreatedAt())
                        .isPublic(collection.isShareable())
                        .build();
    }
}
