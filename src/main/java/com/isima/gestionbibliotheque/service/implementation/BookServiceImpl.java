package com.isima.gestionbibliotheque.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.OperationNotPermittedException;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.OpenLibrarySearchResponse;
import com.isima.gestionbibliotheque.dto.UserBookDto;
import com.isima.gestionbibliotheque.model.*;
import com.isima.gestionbibliotheque.repository.*;
import com.isima.gestionbibliotheque.service.BookService;
import com.isima.gestionbibliotheque.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserBookRepository userBookRepository;
    private final CollectionRepository collectionRepository;
    private final BookFeedbackRepository bookFeedbackRepository;
    private final LoanRepository loanRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;


    private static final String OPEN_LIBRARY_SEARCH_API_URL = "https://openlibrary.org/search.json?q=%s";
    private static final String OPEN_LIBRARY_DETAILS_API_URL = "https://openlibrary.org/api/volumes/brief/isbn/%s.json";



    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            AuthorRepository authorRepository,
            PublisherRepository publisherRepository,
            CoverImageRepository coverImageRepository,
            UserBookRepository userBookRepository,
            CollectionRepository collectionRepository,
            BookFeedbackRepository bookFeedbackRepository,
            LoanRepository loanRepository,
            TagRepository tagRepository,
            UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.coverImageRepository = coverImageRepository;
        this.userBookRepository = userBookRepository;
        this.collectionRepository = collectionRepository;
        this.bookFeedbackRepository = bookFeedbackRepository;
        this.loanRepository = loanRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    @Cacheable("books")
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
    @Cacheable("books")
    public BookDto getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cannot find book with ID %d", bookId))
        );
        return BookDto.fromEntity(book);
    }

    @Override
    public List<UserBookDto> getAllBooksByUserId(Long userId) {
        List<UserBook> userBooks = userBookRepository.findAllByUserId(userId);
        return userBooks.stream().map(UserBookDto::fromEntity).toList();
    }

    @Override
    @Transactional
    @CacheEvict("books")
    public void deleteBookFromLibrary(Long userId, Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByUsername(authentication.getName());

        if (!user.getId().equals(userId)) {
            throw new AccessDeniedException("Forbidden. You don't have permissions to delete this book");
        }

        UserBook  userBook = userBookRepository.findByUserIdAndBookId(userId, bookId);

        if (userBook != null) {
            boolean hasActiveLoans = loanRepository.existsByUserBookAndUserBookStatus(userBook, BookStatus.BORROWED);
            if (hasActiveLoans) {
                throw new OperationNotPermittedException("Cannot delete book because it's currently on loan");
            }

            bookFeedbackRepository.findByUserIdAndBookId(userId, bookId).ifPresent(
                    bookFeedbackRepository::delete
            );

            List<Loan> loans = loanRepository.findAllByUserBookId(userBook.getId());
            log.info("loans number "+loans.size());
            loanRepository.deleteAll(loans);

            List<Tag> tags = tagRepository.findAllByUserBookUserIdAndUserBookBookId(userId, bookId);
            tagRepository.deleteAll(tags);

            List<Collection> collections = collectionRepository.findAllByUserIdAndBooksContaining(userId, userBook.getBook());

            for (Collection collection: collections) {
                collection.getBooks().remove(userBook.getBook());
                collectionRepository.save(collection);
            }

            userBookRepository.delete(userBook);

        } else {
            throw new EntityNotFoundException("Book not found in user's library");
        }

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
            book.setPublishDate(dataNode.get("publish_date").asText());
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
