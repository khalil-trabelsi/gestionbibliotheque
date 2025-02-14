package com.isima.gestionbibliotheque.repository;

import com.isima.gestionbibliotheque.model.AccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessKeyRepository extends JpaRepository<AccessKey, UUID> {
}
