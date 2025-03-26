package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @ManyToOne
    @JsonManagedReference
    @JsonIncludeProperties({"id", "username"})
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"userBooks"})
    private Book book;

    @OneToMany(mappedBy = "userBook")
    private List<Tag> tags = new ArrayList<>();


    @OneToMany(mappedBy = "userBook")
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();


}
