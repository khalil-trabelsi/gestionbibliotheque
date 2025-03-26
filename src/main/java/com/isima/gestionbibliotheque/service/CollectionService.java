package com.isima.gestionbibliotheque.service;

import com.isima.gestionbibliotheque.dto.CollectionDto;
import com.isima.gestionbibliotheque.dto.CreateCollectionDto;
import com.isima.gestionbibliotheque.dto.UpdateCollectionDto;

import java.util.List;

public interface CollectionService {
    List<CollectionDto> getAllCollections();

    List<CollectionDto> getAllCollectionsByBookId(Long bookID);

    List<CollectionDto> getAllCollectionsByUserId(Long userId);

    CollectionDto createCollection(CreateCollectionDto dto, String username);

    CollectionDto updateCollection(UpdateCollectionDto updateCollectionDto, Long collectionId, String key);

    void deleteCollection(Long collectionId);

    CollectionDto getCollectionById(Long collectionId, String key);

    CollectionDto addBookToCollection(Long collectionId, Long bookId);

    void removeBookFromCollection(Long collectionId, Long bookId, String key);

}
