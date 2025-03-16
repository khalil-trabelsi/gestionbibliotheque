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
import java.util.Objects;

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
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    String.format("The book with ID %d does not exist in the user's library. ", borrowBookRequest.getUserBookId())
                            ));

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
                    .expectedReturnDate(LocalDate.now().plusDays(borrowBookRequest.getLoanDurationInDays()))
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

        userBookRepository.findById(loan.getUserBook().getId()).ifPresent(
                (book) -> {
                    book.setStatus(BookStatus.AVAILABLE);
                    userBookRepository.save(book);
                }
        );

        return LoanDto.fromEntity(loanRepository.save(loan));
    }


    @Override
    public List<LoanDto> getBookLoanHistoryByBorrowerId(Long borrowerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findUserByUsername(authentication.getName());
        if (!Objects.equals(currentUser.getId(), borrowerId)) {
            throw new AccessDeniedException("You don't have permissions to access this resource");
        }

        List<Loan> loans = loanRepository.findAllByBorrowerId(borrowerId);

        return loans.stream().map(LoanDto::fromEntity).toList();
    }

    @Override
    public List<LoanDto> getAllReturnedBooks(Long borrowerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var user = userRepository.findUserByUsername(authentication.getName());
            if (!user.getId().equals(borrowerId) ) {
                throw new AccessDeniedException("You don't have permissions to access this resource");
            }
            return loanRepository.findAllReturnedBooks(user.getId()).stream().map(LoanDto::fromEntity).toList();
        }

        throw new AccessDeniedException("Forbidden");
    }

    @Override
    public List<LoanDto> getAllBorrowedBooks(Long borrowerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var user = userRepository.findUserByUsername(authentication.getName());
            if (!user.getId().equals(borrowerId) ) {
                throw new AccessDeniedException("You don't have permissions to access this resource");
            }
            return loanRepository.findAllBorrowedBooks(user.getId()).stream().map(LoanDto::fromEntity).toList();
        }

        throw new AccessDeniedException("Forbidden");
    }

    @Override
    public List<LoanDto> getBookLoanHistoryByBookIdAndUserId(Long bookId, Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findUserByUsername(authentication.getName());
        if (!Objects.equals(currentUser.getId(), userId)) {
            throw new AccessDeniedException("You don't have permissions to access this resource");
        }

        List<Loan> loans = loanRepository.findAllByUserBookUserIdAndUserBookBookId(userId, bookId);

        return loans.stream().map(LoanDto::fromEntity).toList();
    }


}
