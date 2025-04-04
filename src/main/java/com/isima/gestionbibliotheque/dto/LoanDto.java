package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.model.UserBook;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {
    private Long id;

    @JsonIgnoreProperties({"userBooks", "createdAt", "birthDate"})
    private User borrower;

    @JsonIncludeProperties({"id", "book"})
    private UserBook userBook;

    private LocalDate borrowedAt;
    private LocalDate returnedAt;
    private LocalDate expectedReturnDate;
    private boolean returned;

    public static LoanDto fromEntity(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .borrower(loan.getBorrower())
                .userBook(loan.getUserBook())
                .borrowedAt(loan.getBorrowedAt())
                .returnedAt(loan.getReturnedAt())
                .expectedReturnDate(loan.getExpectedReturnDate())
                .returned(loan.isReturned())
                .build();
    }
}
