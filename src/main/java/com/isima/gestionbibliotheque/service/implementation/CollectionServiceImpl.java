package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.dto.CollectionDto;
import com.isima.gestionbibliotheque.dto.CreateCollectionDto;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.repository.CollectionRepository;
import com.isima.gestionbibliotheque.repository.UserBookRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.AccessKeyService;
import com.isima.gestionbibliotheque.service.CollectionService;
import jakarta.persistence.Access;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserBookRepository userBookRepository;
    private final UserRepository userRepository;
    private final AccessKeyService accessKeyService;

    @Autowired
    public CollectionServiceImpl(
            CollectionRepository collectionRepository,
            UserBookRepository userBookRepository,
            UserRepository userRepository,
            AccessKeyService accessKeyService
    ) {
        this.collectionRepository = collectionRepository;
        this.userBookRepository = userBookRepository;
        this.userRepository = userRepository;
        this.accessKeyService = accessKeyService;
    }

    @Override
    public List<CollectionDto> getAllCollections() {
        return collectionRepository.findAll().stream().map(CollectionDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CollectionDto> getAllCollectionsByUserBookId(Long userBookId) {
        return collectionRepository.findAllByUserBooksId(userBookId).stream().map(CollectionDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CollectionDto> getAllCollectionsByUserId(Long userId) {
        return collectionRepository.findAllByUserId(userId).stream().map(CollectionDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public CollectionDto getCollectionById(Long collectionId, String key) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find Collection with id: %d", collectionId), ErrorCode.COLLECTION_NOT_FOUND)
        );
        CollectionDto dto = CollectionDto.fromEntity(collection);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (collection.getUser().getUsername().equals(authentication.getName())) {
                return dto;
            }
        }
        if (collection.isPublic()) {
            return dto;
        }

        if (key != null) {
            AccessKey accessKey = accessKeyService.getAccessKeyById(UUID.fromString(key));
            if (!accessKey.isExpired() && accessKey.getPermissions().contains(Permission.COLLECTION_READ)) {
                return dto;
            }
        }

        throw new RuntimeException("Access Denied");
    }

    @Override
    public CollectionDto createCollection(CreateCollectionDto dto, String username) {
        List<String> userBookErrors = new ArrayList<>();
        List<UserBook> userBooks = new ArrayList<>();

        User user = userRepository.findUserByUsername(username);

        validateUserBooks(dto, userBookErrors, userBooks);
        Collection collection = new Collection();

        collection.setName(dto.getName());
        collection.setDescription(dto.getDescription());
        collection.setUser(user);
        collection.setUserBooks(userBooks);
        collection.setPublic(dto.isPublic());

        collection = collectionRepository.save(collection);
        return CollectionDto.fromEntity(collection);
    }

    @Override
    public CollectionDto updateCollection(CreateCollectionDto dto, Long collectionId, String key) {
        List<String> userBookErrors = new ArrayList<>();
        List<UserBook> userBooks = new ArrayList<>();

        validateUserBooks(dto, userBookErrors, userBooks);

        if (!userBookErrors.isEmpty()) {
            throw new BadRequestException("User book(s) not founds", ErrorCode.USER_BOOK_NOT_FOUND, userBookErrors);
        }

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find collection")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (collection.getUser().getUsername().equals(authentication.getName())) {
                collection.setUserBooks(userBooks);
                collection.setName(dto.getName());
                collection.setDescription(dto.getDescription());
                collection.setPublic(dto.isPublic());
                return CollectionDto.fromEntity(collectionRepository.save(collection));
            }
        }

        if (key != null) {
            AccessKey accessKey = accessKeyService.getAccessKeyById(UUID.fromString(key));
            if (accessKey.getPermissions().contains(Permission.COLLECTION_UPDATE)) {
                collection.setUserBooks(userBooks);
                collection.setName(dto.getName());
                collection.setDescription(dto.getDescription());
                return CollectionDto.fromEntity(collectionRepository.save(collection));
            }
        }

        throw new RuntimeException("Operation denied.");
    }
    @Override
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () ->  new EntityNotFoundException("Cannot find collection")
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getName().equals(collection.getUser().getUsername())) {
                collectionRepository.deleteById(collectionId);
            } else {
                throw new RuntimeException("Action denied");
            }
        }
    }

    private void validateUserBooks(CreateCollectionDto dto, List<String> userBookErrors, List<UserBook> userBooks) {
        if (dto.getUserBookIds() != null) {
            for (Long userBookId: dto.getUserBookIds()) {
                Optional<UserBook> existingUserBook = userBookRepository.findById(userBookId);
                log.info("user Book: "+existingUserBook.isEmpty());
                if (existingUserBook.isEmpty()) {
                    userBookErrors.add(String.format("Cannot find userBook with Id %d", userBookId));
                } else {
                    userBooks.add(existingUserBook.get());
                }
            }
        }

        if (!userBookErrors.isEmpty()) {
            throw new BadRequestException("User book(s) not founds", ErrorCode.USER_BOOK_NOT_FOUND, userBookErrors);
        }
    }

}
