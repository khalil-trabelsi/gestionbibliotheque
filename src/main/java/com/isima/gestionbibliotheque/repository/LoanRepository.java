package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Loan;
import com.isima.gestionbibliotheque.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByEmprunteur_Id(Long emprunteurId);
    List<Loan> findByUserId(long userId);
    List<Loan> findByStatus(LoanStatus status);
}
