package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserBookUserIdAndUserBookBookId(Long userId, Long bookId);

    List<Tag> findAllByUserBookBookId(Long bookId);
}
