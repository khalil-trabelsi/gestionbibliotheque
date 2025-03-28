package com.isima.gestionbibliotheque.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User borrower;

    @ManyToOne
    @JoinColumn( nullable = false)
    private UserBook userBook;

    @Column(nullable = false)
    private LocalDate borrowedAt;

    private LocalDate returnedAt;


    private LocalDate expectedReturnDate;

    private boolean returned;

    @PrePersist
    protected void onCreate() {
        borrowedAt = LocalDate.now();
    }
}
