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
    @Column(unique = true)
    private String isbn;
    private String title;
    private String subtitle;
    private String numberOfPages;
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

    @OneToOne(mappedBy = "book")
    private CoverImage coverImage;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookFeedback> bookFeedback;


    @Transient
    public double getRate() {
        if (bookFeedback == null || bookFeedback.isEmpty()) {
            return 0.0;
        }

        var rating = this.bookFeedback.stream().mapToDouble(BookFeedback::getRating).average().orElse(0.0);

        return Math.round(rating * 10.0) / 10.0;
    }


}
