package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.CollectionDto;
import com.isima.gestionbibliotheque.dto.CreateCollectionDto;

import java.util.List;

public interface CollectionService {
    List<CollectionDto> getAllCollections();

    List<CollectionDto> getAllCollectionsByUserBookId(Long userBookId);

    List<CollectionDto> getAllCollectionsByUserId(Long userId);

    CollectionDto createCollection(CreateCollectionDto dto, String username);

    CollectionDto updateCollection(CreateCollectionDto collectionDto, Long collectionId, String key);

    void deleteCollection(Long collectionId);

    CollectionDto getCollectionById(Long collectionId, String key);

}
