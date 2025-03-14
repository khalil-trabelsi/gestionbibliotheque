package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByUserId(Long userId);
    List<Collection> findAllByBooksId(Long userBookId);
}
