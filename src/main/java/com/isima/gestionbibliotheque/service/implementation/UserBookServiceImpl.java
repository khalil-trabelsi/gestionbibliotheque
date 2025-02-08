package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private static final String API_URL = "https://openlibrary.org/api/volumes/brief/isbn/%s.json";



    @Autowired
    public UserBookServiceImpl(
            UserBookRepository userBookRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository
            ) {
        this.userBookRepository = userBookRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }
    @Override
    public List<UserBook> getAllBooksByUserId(Long userId) {
        return userBookRepository.findAllByUserId(userId);
    }


    @Override
    public UserBook addBook(String isbn, Long userId, String status, String location, int rating) throws JsonProcessingException {
        String url = String.format(API_URL, isbn);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        String jsonResponse = restTemplate.getForObject(url, String.class);



        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode recordsNode = rootNode.path("records");
        Iterator<String> fieldNames = recordsNode.fieldNames();
        Book existingBook = bookRepository.findBookByIsbn(isbn);
        if (existingBook == null) {
            existingBook = new Book();
        }

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



        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException(String.format("Cannot find user with Id %d", userId))
        );
        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setRating(rating);
        userBook.setLocation(location);
        userBook.setStatus(status);
        userBook.setBook(existingBook);


        return userBookRepository.save(userBook);
    }

    @Override
    public void deleteBook(Long bookId) {

    }
}
