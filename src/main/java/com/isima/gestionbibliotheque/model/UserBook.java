package com.isima.gestionbibliotheque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBook {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private int rating;
    private String status;
    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;
    @OneToMany(mappedBy = "userBook")
    private List<Tag> tag = new ArrayList<>();



}
