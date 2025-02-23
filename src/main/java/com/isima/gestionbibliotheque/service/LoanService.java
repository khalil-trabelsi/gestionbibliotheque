package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import com.isima.gestionbibliotheque.dto.LoanDto;

import java.util.List;

public interface LoanService {
    LoanDto borrowBook(BorrowBookRequest borrowBookRequest);
    LoanDto returnBorrowedBook(Long loanId);
    List<LoanDto> getBorrowedBooksByUserId(Long borrowerId);
    List<LoanDto> getUserLoans(Long id);

}
