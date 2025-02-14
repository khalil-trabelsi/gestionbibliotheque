package com.isima.gestionbibliotheque.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "collections")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private boolean isPublic;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<UserBook> userBooks = new ArrayList<>();

    @OneToMany(mappedBy = "collection",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AccessKey> accessKeys = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

}
