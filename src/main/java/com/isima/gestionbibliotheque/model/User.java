package com.isima.gestionbibliotheque.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private Date createdAt;
    @OneToMany(mappedBy = "user")
    @Column(name = "user_book")
    private List<UserBook> userBooks = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    @Column(name = "tag")
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Collection> collections = new ArrayList<>();

}


