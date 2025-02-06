package com.isima.gestionbibliotheque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;
    private Date createdAt;
    @ManyToOne
    private User user;
    @ManyToMany(mappedBy = "collections")
    private List<Book> books = new ArrayList<>();
}
