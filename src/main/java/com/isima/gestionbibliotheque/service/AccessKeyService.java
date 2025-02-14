package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.SharedCollectionResponseDto;
import com.isima.gestionbibliotheque.model.AccessKey;
import com.isima.gestionbibliotheque.model.Permission;

import java.util.Set;
import java.util.UUID;

public interface AccessKeyService {
    AccessKey getAccessKeyById(UUID id);
    SharedCollectionResponseDto createAccessKey(Long collectionId, Set<String> permissions, String serverUri);

}
