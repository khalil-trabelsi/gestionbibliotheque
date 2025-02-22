package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.LoanStatus;
import com.isima.gestionbibliotheque.model.User;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanServiceImpl(
            BookRepository bookRepository,
            UserRepository userRepository,
            LoanRepository loanRepository
    ) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public LoanDto addLoan(LoanDto loanDto) {
        User emprunteur = userRepository.findById(loanDto.getEmprunteurId())
                .orElseThrow(() -> new RuntimeException("Emprunteur not found"));
        User owner = userRepository.findById(loanDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(loanDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Loan loan = Loan.builder()
                .emprunteur(emprunteur)
                .user(owner)
                .book(book)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .status(LoanStatus.EN_COURS)
                .build();

        return LoanDto.fromEntity(loanRepository.save(loan));
    }

    @Override
    public LoanDto returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("loan record not found"));

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETOURNE);

        return LoanDto.fromEntity(loanRepository.save(loan));
    }

    @Override
    public List<LoanDto> getEmprunteurLoans(Long emprunteurId) {
        return loanRepository.findByEmprunteur_Id(emprunteurId).stream()
                .map(LoanDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getUserLoans(Long id) {
        return loanRepository.findByUserId(id).stream()
                .map(LoanDto::fromEntity)
                .collect(Collectors.toList());
    }
}
