package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.LoanService;
import com.isima.gestionbibliotheque.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/loan")
@Slf4j
public class LoanController {
    private final LoanService loanService;
    private final UserRepository userRepository;
    @Autowired
    public LoanController(
            LoanService loanService, UserRepository userRepository
    ) {
        this.loanService = loanService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<LoanDto> borrowBook(@RequestBody BorrowBookRequest borrowBookRequest) {
        return ResponseEntity.ok(loanService.borrowBook(borrowBookRequest));
    }

    @PutMapping("/{loanId}/return_book")
    public ResponseEntity<LoanDto> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBorrowedBook(loanId));
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<List<LoanDto>> getBorrowerLoans(@PathVariable Long borrowerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            User user = userRepository.findUserByUsername(username);

            if (!Objects.equals(user.getId(), borrowerId)) {
                throw new AccessDeniedException("You don't have permissions to access this resource");
            }

            List<LoanDto> loans = loanService.getBorrowedBooksByUserId(user.getId());

            return ResponseEntity.ok(loans);
        }

        throw new AccessDeniedException("");

    }


}
