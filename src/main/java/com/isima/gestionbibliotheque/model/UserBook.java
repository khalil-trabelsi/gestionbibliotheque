package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    private double rating;

    private String comment;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JsonManagedReference
    private User user;

    @ManyToOne
    private Book book;

    @OneToMany(mappedBy = "userBook")
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany(mappedBy = "userBooks")
    @JsonIgnore
    private List<Collection> collections = new ArrayList<>();

    @OneToMany(mappedBy = "userBook")
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();
}
