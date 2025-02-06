package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JsonBackReference
    @OneToMany(mappedBy = "book")
    private List<UserBook> userBooks = new ArrayList<>();
    @ManyToMany
    private List<Collection> collections = new ArrayList<>();
}
