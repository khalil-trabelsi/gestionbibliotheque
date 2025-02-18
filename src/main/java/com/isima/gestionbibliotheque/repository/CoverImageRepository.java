package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.CoverImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverImageRepository extends JpaRepository<CoverImage, Long> {
}
