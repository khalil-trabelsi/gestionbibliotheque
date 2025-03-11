package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBorrowerId(Long borrowerId);

    @Query("""
        SELECT loan FROM Loan loan where loan.borrower.id = :borrowerId and loan.returned = true
    """)
    List<Loan> findAllReturnedBooks(Long borrowerId);

    @Query("""
        SELECT loan FROM Loan loan where loan.borrower.id = :borrowerId and loan.returned = false
    """)
    List<Loan> findAllBorrowedBooks(Long borrowerId);

    List<Loan> findAllByUserBookUserIdAndUserBookBookId(Long userId, Long bookId);

    List<Loan> findAllByBorrowerId(Long userId);

}
