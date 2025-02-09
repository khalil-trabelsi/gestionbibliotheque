package com.isima.gestionbibliotheque.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.dto.BookRequest;
import com.isima.gestionbibliotheque.dto.UserBookDto;
import com.isima.gestionbibliotheque.model.CustomUserDetails;
import com.isima.gestionbibliotheque.model.UserBook;
import com.isima.gestionbibliotheque.service.UserBookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/user_books")
@Tag(name = "UserBook")
public class UserBookController {

    private final UserBookService userBookService;

    public UserBookController(UserBookService userBookService) {
        this.userBookService = userBookService;
    }
    @GetMapping("/{userBookId}")
    public UserBookDto getUserBooKById(@PathVariable Long userBookId) {
        return UserBookDto.fromEntity(userBookService.getUserBookById(userBookId));
    }
    @GetMapping("/users/{userId}")
    public List<UserBookDto> getAllUserBooksByUserId(@PathVariable Long userId) {
        return userBookService.getAllBooksByUserId(userId).stream().map(UserBookDto::fromEntity).collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserBookDto> addUserBook(@RequestBody BookRequest bookRequest, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        CustomUserDetails user = (CustomUserDetails) userDetails;
        UserBook userBook = userBookService.addUserBook(
                bookRequest.getIsbn(),
                user.getId(),
                bookRequest.getStatus(),
                bookRequest.getLocation(),
                bookRequest.getRating()
        );
        UserBookDto userBookDto = UserBookDto.fromEntity(userBook);
            return new ResponseEntity<>(userBookDto, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<UserBookDto> updateUserBook(@RequestBody BookRequest bookRequest) {
        return null;
    }

    @DeleteMapping("/{userBookId}")
    @PreAuthorize("isAuthenticated()")
    public void deleteUserBook(@PathVariable Long userBookId) {
        userBookService.deleteUserBook(userBookId);
    }


}
