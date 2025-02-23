package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.OperationNotPermittedException;
import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;

    @Autowired
    public LoanServiceImpl(
            UserRepository userRepository,
            LoanRepository loanRepository,
            UserBookRepository userBookRepository
    ) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.userBookRepository = userBookRepository;
    }

    @Override
    @Transactional
    public LoanDto borrowBook(BorrowBookRequest borrowBookRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserBook userBook = userBookRepository.findById(borrowBookRequest.getUserBookId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("User Book %d not found ", borrowBookRequest.getUserBookId())));
            User user = userRepository.findUserByUsername(authentication.getName());
            if (userBook.getUser().getUsername().equals(user.getUsername())) {
                throw new OperationNotPermittedException("You cannot borrow your own book");
            }

            if (userBook.getStatus().equals(BookStatus.BORROWED)) {
                throw new OperationNotPermittedException("The requested book is already borrowed");
            }



            Loan loan = Loan.builder()
                    .borrower(user)
                    .userBook(userBook)
                    .expectedReturnDate(LocalDate.now().plusWeeks(2))
                    .returned(false)
                    .build();

            userBook.setStatus(BookStatus.BORROWED);
            userBookRepository.save(userBook);

            return LoanDto.fromEntity(loanRepository.save(loan));
        }



        throw new AccessDeniedException("Access denied");

    }

    @Override
    public LoanDto returnBorrowedBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId).orElseThrow(
                () -> new OperationNotPermittedException("You did not borrow this book")
        );

        if (loan.isReturned()) {
            throw new OperationNotPermittedException("Book is already returned");
        }

        loan.setReturnedAt(LocalDate.now());
        loan.setReturned(true);

        return LoanDto.fromEntity(loanRepository.save(loan));
    }

    @Override
    public List<LoanDto> getBorrowedBooksByUserId(Long borrowerId) {
        return loanRepository.findByBorrowerId(borrowerId).stream()
                .map(LoanDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LoanDto> getUserLoans(Long id) {
        return loanRepository.findByBorrowerId(id).stream()
                .map(LoanDto::fromEntity)
                .collect(Collectors.toList());
    }
}
