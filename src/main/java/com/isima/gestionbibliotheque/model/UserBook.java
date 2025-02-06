package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private int rating;
    private String status;
    @JsonManagedReference
    @ManyToOne
    private User user;
    @JsonManagedReference
    @ManyToOne
    private Book book;
    @OneToMany(mappedBy = "userBook")
    private List<Tag> tag = new ArrayList<>();



}
