package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.controller.api.LoanApiDocs;
import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.LoanService;
import com.isima.gestionbibliotheque.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class LoanController implements LoanApiDocs {
    private final LoanService loanService;
    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<List<LoanDto>> getBorrowerLoanHistory(
            Authentication authentication
    ) {
        User borrower = (User) authentication.getPrincipal();

        return ResponseEntity.ok(loanService.getBookLoanHistoryByBorrowerId(borrower.getId()));
    }

    @Override
    public ResponseEntity<List<LoanDto>> getAllBorrowedBooks(@PathVariable Long borrowerId) {
        return ResponseEntity.ok(loanService.getAllBorrowedBooks(borrowerId));
    }

    @Override

    public ResponseEntity<List<LoanDto>> getAllReturnedBooks(@PathVariable Long borrowerId) {
        return ResponseEntity.ok(loanService.getAllReturnedBooks(borrowerId));
    }

    @Override

    public ResponseEntity<List<LoanDto>> getBookLoanHistory(
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "userId") Long userId
    ) {
        return ResponseEntity.ok(loanService.getBookLoanHistoryByBookIdAndUserId(bookId, userId));
    }

    @Override

    public ResponseEntity<LoanDto> borrowBook(
            @Valid @RequestBody BorrowBookRequest borrowBookRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error: bindingResult.getFieldErrors()) {
                errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage()));
            }
            throw new BadRequestException("Invalid input data", null, errors);
        }
        return ResponseEntity.ok(loanService.borrowBook(borrowBookRequest));
    }

    @Override
    public ResponseEntity<LoanDto> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBorrowedBook(loanId));
    }

}
