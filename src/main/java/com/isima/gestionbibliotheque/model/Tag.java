package com.isima.gestionbibliotheque.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private String color;
    @ManyToOne
    private User user;
    @ManyToOne
    private UserBook userBook;
}
