package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.Loan;

import java.util.List;

public interface LoanService {
    LoanDto borrowBook(BorrowBookRequest borrowBookRequest);
    LoanDto returnBorrowedBook(Long loanId);
    List<LoanDto> getBorrowedBooksByUserId(Long borrowerId);
    List<LoanDto> getAllReturnedBooks(Long borrowerId);
    List<LoanDto> getAllBorrowedBooks(Long borrowerId);

    List<LoanDto> getUserLoans(Long borrowerId);


}
