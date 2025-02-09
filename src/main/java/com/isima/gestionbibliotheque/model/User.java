package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate birthDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @Column(name = "user_book")
    private List<UserBook> userBooks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @Column(name = "tag")
    private List<Tag> tags = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Collection> collections = new ArrayList<>();

}


