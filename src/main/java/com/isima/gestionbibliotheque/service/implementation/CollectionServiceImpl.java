package com.isima.gestionbibliotheque.service.implementation;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.dto.CollectionDto;
import com.isima.gestionbibliotheque.dto.CreateCollectionDto;
import com.isima.gestionbibliotheque.dto.UpdateCollectionDto;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.repository.BookRepository;
import com.isima.gestionbibliotheque.repository.CollectionRepository;
import com.isima.gestionbibliotheque.repository.UserRepository;
import com.isima.gestionbibliotheque.service.AccessKeyService;
import com.isima.gestionbibliotheque.service.CollectionService;
import com.isima.gestionbibliotheque.service.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final AccessKeyService accessKeyService;

    private final UserBookService userBookService;

    private final BookRepository bookRepository;


    @Autowired
    public CollectionServiceImpl(
            CollectionRepository collectionRepository,
            UserRepository userRepository,
            AccessKeyService accessKeyService,
            UserBookService userBookService, BookRepository bookRepository
    ) {
        this.collectionRepository = collectionRepository;
        this.userRepository = userRepository;
        this.accessKeyService = accessKeyService;
        this.userBookService = userBookService;
        this.bookRepository = bookRepository;
    }

    @Override
    @Cacheable(value = "collections")
    public List<CollectionDto> getAllCollections() {
        List<Collection> collections = collectionRepository.findAllByShareable(true);
        log.info("Collection size : "+ collections.size());
        return collections.stream().map(CollectionDto::fromEntity).toList();

    }

    @Override
    public List<CollectionDto> getAllCollectionsByBookId(Long bookID) {
        return collectionRepository.findAllByBooksId(bookID).stream().filter(Collection::isShareable).map(CollectionDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<CollectionDto> getAllCollectionsByUserId(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find user with id %d", userId))
        );

        List<Collection> collections = collectionRepository.findAllByUserId(userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getName().equals(user.getUsername())) {
                return collections.stream().map(CollectionDto::fromEntity).collect(Collectors.toList());
            }
        }

        return collections.stream().filter(Collection::isShareable).map(CollectionDto::fromEntity).collect(Collectors.toList());

    }

    @Override
    public CollectionDto getCollectionById(Long collectionId, String key) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find Collection with id: %d", collectionId), ErrorCode.COLLECTION_NOT_FOUND)
        );
        CollectionDto dto = CollectionDto.fromEntity(collection);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (collection.getUser().getUsername().equals(authentication.getName()) || collection.isShareable()) {
                return dto;
            }
        }

        if (key != null) {
            AccessKey accessKey = accessKeyService.getAccessKeyById(UUID.fromString(key));
            if (!accessKey.isExpired() && accessKey.getPermissions().contains(Permission.COLLECTION_READ)) {
                return dto;
            }
        }

        throw new AccessDeniedException("You don't have permission to access this collection");
    }

    @Override
    @Transactional
    @CacheEvict(value = "collections", allEntries = true)
    public CollectionDto createCollection(CreateCollectionDto dto, String username) {

        User user = userRepository.findUserByUsername(username);
        Collection collection = new Collection();

        collection.setName(dto.getName());
        collection.setDescription(dto.getDescription());
        collection.setUser(user);
        collection.setShareable(dto.isShareable());

        collection = collectionRepository.save(collection);
        return CollectionDto.fromEntity(collection);
    }

    @Override
    @Transactional
    @CachePut(value = "collections", key = "#collectionId")
    public CollectionDto updateCollection(UpdateCollectionDto dto, Long collectionId, String key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Access check: if the user is neither authenticated nor authorized via a key
        if (!(authentication != null && authentication.isAuthenticated()) && key == null) {
            throw new AccessDeniedException("You don't have permission to modify this collection");
        }

        List<String> bookErrors = new ArrayList<>();
        List<Book> existingBooks = new ArrayList<>();

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find collection")
        );

        // Check the existence of books
        if (dto.getBooksId() != null) {
            for (Long bookId: dto.getBooksId()) {
                try {
                    Book existingBook = bookRepository.findById(bookId).orElseThrow(
                            () -> new EntityNotFoundException("Cannot find book")
                    );
                    existingBooks.add(existingBook);

                } catch (Exception e) {
                    bookErrors.add(e.getMessage());
                }
            }
        }

        if (!bookErrors.isEmpty()) {
            throw new EntityNotFoundException("Some books not founds", ErrorCode.USER_BOOK_NOT_FOUND, bookErrors);
        }

        // Check if user is allowed to modify the collection
        if (authentication != null && authentication.isAuthenticated()) {

            if (collection.getUser().getUsername().equals(authentication.getName())) {
                // associate a book to the current user
                existingBooks.forEach(book -> {
                    userBookService.createUserBook(book.getId(), authentication.getName());
                });
                collection.setBooks(existingBooks);
                collection.setName(dto.getName());
                collection.setDescription(dto.getDescription());
                collection.setShareable(dto.isShareable());
                return CollectionDto.fromEntity(collectionRepository.save(collection));
            }
        }

        // Check access with a key
        if (key != null) {
            AccessKey accessKey = accessKeyService.getAccessKeyById(UUID.fromString(key));
            if (!accessKey.isExpired() && accessKey.getPermissions().contains(Permission.COLLECTION_UPDATE)) {
                collection.setBooks(existingBooks);
                collection.setName(dto.getName());
                collection.setDescription(dto.getDescription());
                return CollectionDto.fromEntity(collectionRepository.save(collection));
            }
        }

        throw new AccessDeniedException("You don't have permission to modify this collection.");
    }

    @Override
    @CacheEvict(value = "collections", allEntries = true)
    public void deleteCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () ->  new EntityNotFoundException(String.format("Cannot find collection %d", collectionId))
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getName().equals(collection.getUser().getUsername())) {
                collectionRepository.deleteById(collectionId);
            } else {
                throw new AccessDeniedException("You don't have permission to delete this collection");
            }
        }
    }



    @Override
    @CacheEvict(value = "collections", allEntries = true)
    public CollectionDto addBookToCollection(Long collectionId, Long bookId) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find collection with ID %d", collectionId))
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (collection.getUser().getUsername().equals(authentication.getName())) {

                Book existingBook = bookRepository.findById(bookId).orElseThrow(
                        () -> new EntityNotFoundException("Cannot find book")
                );
                if (!collection.getBooks().contains(existingBook)) {
                    userBookService.createUserBook(bookId, authentication.getName());

                    collection.getBooks().add(existingBook);
                    collectionRepository.save(collection);
                }

                return CollectionDto.fromEntity(collection);
            }
        }


        throw new AccessDeniedException("You don't have permission to modify this collection");

    }

    @Override
    public void removeBookFromCollection(Long collectionId, Long bookId, String key) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Access check: if the user is neither authenticated nor authorized via a key
        if (!(authentication != null && authentication.isAuthenticated()) && key == null) {
            throw new AccessDeniedException("You don't have permission to modify this collection");
        }

        Collection collection = collectionRepository.findById(collectionId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find collection with ID %d", collectionId))
        );

        Book existingBook = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find book")
        );

        // Check permissions, either by user or by access key
        if (hasPermissionToModifyCollection(authentication, collection, key)) {
            if (collection.getBooks().contains(existingBook)) {

                collection.getBooks().remove(existingBook);
                collectionRepository.save(collection);
            } else {
                throw new EntityNotFoundException("Book not found in this collection");
            }
        } else {
            throw new AccessDeniedException("You don't have permission to modify this collection");
        }
    }


    private boolean hasPermissionToModifyCollection(Authentication authentication, Collection collection, String key) {
        if (authentication != null && authentication.isAuthenticated()) {
            return collection.getUser().getUsername().equals(authentication.getName());
        }

        if (key != null) {
            AccessKey accessKey = accessKeyService.getAccessKeyById(UUID.fromString(key));
            return !accessKey.isExpired() && accessKey.getPermissions().contains(Permission.COLLECTION_UPDATE) ;
        }

        return false;
    }
}
