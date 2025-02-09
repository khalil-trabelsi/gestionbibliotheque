package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate publishDate;
    @ManyToMany
    private List<Publisher> publishers;
    private String description;
    private String coverImageUrl;
    private Date createdAt;
    @ManyToMany
    private List<Author> authors = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<UserBook> userBooks = new ArrayList<>();

}
