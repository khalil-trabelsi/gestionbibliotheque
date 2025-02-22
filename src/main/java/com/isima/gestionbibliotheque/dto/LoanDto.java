package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.LoanStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {
    private Long id;
    private Long emprunteurId;
    private Long userId;
    private Long bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;

    public static LoanDto fromEntity(Loan loan) {
        return LoanDto.builder()
                .id(loan.getId())
                .emprunteurId(loan.getEmprunteur().getId())
                .userId(loan.getUser().getId())
                .bookId(loan.getBook().getId())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus())
                .build();
    }
}
