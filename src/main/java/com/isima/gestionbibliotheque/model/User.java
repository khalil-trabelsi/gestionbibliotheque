package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private Date createdAt;
    @JsonBackReference
    @OneToMany(mappedBy = "user")
    @Column(name = "user_book")
    private List<UserBook> userBooks = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    @Column(name = "tag")
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Collection> collections = new ArrayList<>();

}


