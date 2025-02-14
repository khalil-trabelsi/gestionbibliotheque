package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private User user;
    private List<UserBook> userBooks;

    public static Collection toEntity(CollectionDto collectionDto) {

        Collection collection = new Collection();

        collection.setId(collectionDto.getId());
        collection.setUser(collectionDto.getUser());
        collection.setName(collectionDto.getName());
        collection.setPublic(collectionDto.isPublic());
        collection.setDescription(collectionDto.getDescription());
        collection.setCreatedAt(collectionDto.getCreatedAt());
        collection.setUserBooks(collectionDto.getUserBooks());

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
                        .userBooks(collection.getUserBooks())
                        .createdAt(collection.getCreatedAt())
                        .isPublic(collection.isPublic())
                        .build();
    }
}
