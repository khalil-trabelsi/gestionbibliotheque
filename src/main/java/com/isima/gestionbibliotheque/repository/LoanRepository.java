package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBorrowerId(Long borrowerId);

//    Optional<Loan> findByOwnerId(Long userId);
}
