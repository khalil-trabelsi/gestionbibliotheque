package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.LoanService;
import com.isima.gestionbibliotheque.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<LoanDto> empruntBook(@RequestBody LoanDto loanDto) {
        return ResponseEntity.ok(loanService.addLoan(loanDto));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<LoanDto> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnLoan(id));
    }

    @GetMapping("/emprunteur")
    public ResponseEntity<List<LoanDto>> getEmprunteurLoans() {
        // Récupérer l'utilisateur actuellement authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Nom d'utilisateur

        // Trouver l'utilisateur à partir du nom d'utilisateur
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Récupérer les emprunts de cet utilisateur
        Long emprunteurId = user.getId();
        List<LoanDto> loans = loanService.getEmprunteurLoans(emprunteurId);

        return ResponseEntity.ok(loans);

    }

    @GetMapping("/user")
    public ResponseEntity<List<LoanDto>> getUserLoans() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Long userId = user.getId();
        List<LoanDto> loans = loanService.getUserLoans(userId);

        return ResponseEntity.ok(loans);
    }


}
