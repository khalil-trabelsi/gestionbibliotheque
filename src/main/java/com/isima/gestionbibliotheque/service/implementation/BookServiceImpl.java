package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.OpenLibrarySearchResponse;
import com.isima.gestionbibliotheque.helpers.DateParser;
import com.isima.gestionbibliotheque.model.Author;
import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.CoverImage;
import com.isima.gestionbibliotheque.model.Publisher;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CoverImageRepository coverImageRepository;
    private static final String OPEN_LIBRARY_SEARCH_API_URL = "https://openlibrary.org/search.json?q=%s";
    private static final String OPEN_LIBRARY_DETAILS_API_URL = "https://openlibrary.org/api/volumes/brief/isbn/%s.json";



    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository,
            CoverImageRepository coverImageRepository
    ) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.coverImageRepository = coverImageRepository;
    }

    @Override
    @Transactional
    public List<BookDto> findBooks(String isbn, String title, String authorName, String publisherName) throws JsonProcessingException {
        List<BookDto> result = new ArrayList<>();
        log.info("book title: "+title);
        Book existingBook;
        if (isbn != null) {
            existingBook = bookRepository.findBookByIsbn(isbn);
            if (existingBook != null) {
                result.add(BookDto.fromEntity(existingBook));
            } else {
                String bookDetailsResponse = restTemplate.getForObject(String.format(OPEN_LIBRARY_DETAILS_API_URL, isbn), String.class);
                result.add(BookDto.fromEntity(saveBook(bookDetailsResponse, isbn)));
            }
            return result;
        }

        String url = title != null && !title.isBlank() ?
                String.format(OPEN_LIBRARY_SEARCH_API_URL, title) :
                authorName != null && !authorName.isBlank() ? authorName : publisherName  ;
        OpenLibrarySearchResponse searchResponse = restTemplate.getForObject(url, OpenLibrarySearchResponse.class);
        List<String> foundBookIsbns = searchResponse.getBookIsbn();
        for (String foundIsbn: foundBookIsbns) {
            existingBook = bookRepository.findBookByIsbn(foundIsbn);
            if (existingBook != null) {
                result.add(BookDto.fromEntity(existingBook));
            } else {
                String bookDetailsResponse = restTemplate.getForObject(String.format(OPEN_LIBRARY_DETAILS_API_URL, foundIsbn), String.class);
                result.add(BookDto.fromEntity(saveBook(bookDetailsResponse, foundIsbn)));
            }
        }

        if (!result.isEmpty()) {
            return result;
        }

        throw new EntityNotFoundException("Cannot find books with your criteria");

    }


    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::fromEntity).toList();
    }

    @Override
    public BookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with ID %d", bookId))
        );
        return BookDto.fromEntity(book);
    }

    public Book saveBook(String bookDetailsResponse, String isbn) throws JsonProcessingException {
        Book book = new Book();

        JsonNode rootNode = objectMapper.readTree(bookDetailsResponse);
        JsonNode recordsNode = rootNode.path("records");
        Iterator<String> fieldNames = recordsNode.fieldNames();

        if (fieldNames.hasNext()) {
            String firstKey = fieldNames.next();
            JsonNode dataNode = recordsNode.path(firstKey).path("data");
            JsonNode authorsNode = dataNode.get("authors");
            JsonNode publishersNode = dataNode.get("publishers");
            JsonNode coverImage = dataNode.get("cover");

            if (authorsNode != null && authorsNode.isArray()) {
                List<Author> authors = new ArrayList<>();
                for (JsonNode authorNode: authorsNode) {
                    String retrievedAuthorName = authorNode.get("name").asText();
                    String authorUrl = authorNode.get("url").asText();
                    Author author = new Author();
                    author.setName(retrievedAuthorName);
                    author.setUrl(authorUrl);
                    authorRepository.save(author);
                    authors.add(author);
                }
                book.setAuthors(authors);
            }

            if (publishersNode != null && publishersNode.isArray()) {
                List<Publisher> publishers = new ArrayList<>();
                for (JsonNode publisherNode: publishersNode) {
                    String retrievedPublisherName = publisherNode.get("name").asText();
                    Publisher publisher = new Publisher();
                    publisher.setName(retrievedPublisherName);
                    publisherRepository.save(publisher);
                    publishers.add(publisher);
                }
                book.setPublishers(publishers);
            }


            if (coverImage != null) {
                CoverImage coverImage1 = CoverImage
                        .builder()
                        .small(coverImage.get("small").asText())
                        .medium(coverImage.get("medium").asText())
                        .large(coverImage.get("large").asText())
                        .build();
                coverImage1 = coverImageRepository.save(coverImage1);
                book.setCoverImage(coverImage1);

            }

            if (dataNode.get("number_of_pages") != null) {
                book.setNumberOfPages(dataNode.get("number_of_pages").asText());
            }
            book.setPublishDate(DateParser.parseDate(dataNode.get("publish_date").asText()));
            book.setCreatedAt(new Date());
            book.setTitle(dataNode.get("title").asText());
            book.setIsbn(isbn);

            if (dataNode.get("subtitle") != null) {
                book.setSubtitle(dataNode.get("subtitle").asText());
            }

        }
        return bookRepository.save(book);
    }




}
