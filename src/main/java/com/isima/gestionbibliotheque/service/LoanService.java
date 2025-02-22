package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.LoanDto;

import java.util.List;

public interface LoanService {
    LoanDto addLoan(LoanDto loan);
    LoanDto returnLoan(Long loanId);
    List<LoanDto> getEmprunteurLoans(Long emprunteurId);
    List<LoanDto> getUserLoans(Long id);

}
