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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User borrower;

    @ManyToOne
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
