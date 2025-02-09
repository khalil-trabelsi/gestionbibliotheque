package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class UserBookServiceImpl implements UserBookService {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final TagRepository tagRepository;
    private static final String API_URL = "https://openlibrary.org/api/volumes/brief/isbn/%s.json";

    @Autowired
    public UserBookServiceImpl(
            UserBookRepository userBookRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository,
            TagRepository tagRepository
            ) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.tagRepository = tagRepository;
    }
    @Override
    public List<UserBook> getAllBooksByUserId(Long userId) {
        // check if user already exists; else throw an exception
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format("Cannot find user with Id: %d", userId)));
        return userBookRepository.findAllByUserId(userId);
    }
    @Override
    public UserBook getUserBookById(Long userBookId) {
        return userBookRepository.findById(userBookId).orElseThrow(() -> new EntityNotFoundException("Cannot find UserBook with Id: "+userBookId, ErrorCode.USER_BOOK_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserBook addUserBook(String isbn, Long userId, String status, String location, int rating) throws JsonProcessingException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find user with Id %d", userId))
        );

        Book existingBook = bookRepository.findBookByIsbn(isbn);

        if (existingBook == null) {
            existingBook = new Book();
            // Fetch book details from Open Library api
            String url = String.format(API_URL, isbn);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
            String response = restTemplate.getForObject(url, String.class);
            // Transform response To Json
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode recordsNode = rootNode.path("records");
            Iterator<String> fieldNames = recordsNode.fieldNames();

            if (fieldNames.hasNext()) {
                String firstKey = fieldNames.next();
                JsonNode dataNode = recordsNode.path(firstKey).path("data");
                JsonNode authorsNode = dataNode.get("authors");
                JsonNode publishersNode = dataNode.get("publishers");

                if (authorsNode != null && authorsNode.isArray()) {
                    List<Author> authors = new ArrayList<>();
                    for (JsonNode authorNode: authorsNode) {
                        String authorName = authorNode.get("name").asText();
                        String authorUrl = authorNode.get("url").asText();
                        Author author = new Author();
                        author.setName(authorName);
                        author.setUrl(authorUrl);
                        authorRepository.save(author);
                        authors.add(author);
                    }
                    existingBook.setAuthors(authors);
                }

                if (publishersNode != null && publishersNode.isArray()) {
                    List<Publisher> publishers = new ArrayList<>();
                    for (JsonNode publisherNode: publishersNode) {
                        String publisherName = publisherNode.get("name").asText();
                        Publisher publisher = new Publisher();
                        publisher.setName(publisherName);
                        publisherRepository.save(publisher);
                        publishers.add(publisher);
                    }
                    existingBook.setPublishers(publishers);
                }

                existingBook.setPublishDate(LocalDate.parse(dataNode.get("publish_date").asText(), formatter));
                existingBook.setCreatedAt(new Date());
                existingBook.setTitle(dataNode.get("title").asText());
                existingBook.setIsbn(isbn);
                existingBook.setSubtitle(dataNode.get("subtitle").asText());
                existingBook = bookRepository.save(existingBook);


            }
        }

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setRating(rating);
        userBook.setLocation(location);
        userBook.setStatus(status);
        userBook.setBook(existingBook);

        return userBookRepository.save(userBook);
    }

    @Transactional
    public void deleteUserBook(Long userBookId) {
        UserBook userBook = userBookRepository.findById(userBookId).orElseThrow(
                () -> new EntityNotFoundException("Cannot find UserBook with ID " + userBookId)
        );
        if (userBook.getTags() != null) {
            for (Tag tag : userBook.getTags()) {
                tagRepository.deleteById(tag.getId());
            }
        }

        userBookRepository.delete(userBook);
    }

}
