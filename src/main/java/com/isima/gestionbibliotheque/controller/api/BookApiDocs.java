package com.isima.gestionbibliotheque.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.isima.gestionbibliotheque.Exception.ErrorEntity;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.UserBookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Books", description = "API for managing books")
@RequestMapping("/api/books")
public interface BookApiDocs {

    @Operation(
            summary = "Retrieve all available books",
            description = "Returns a list of books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }

    )
    @GetMapping
    ResponseEntity<List<BookDto>> getAllBooks();

    @Operation(
            summary = "Retrieve a book based on its Id",
            description = "Returns a book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    schema = @Schema(implementation = BookDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book not found. The specified book ID does not exist",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }

    )
    @GetMapping("/{bookId}")
    ResponseEntity<BookDto> getBookById(@PathVariable Long bookId);

    @Operation(
            summary = "Retrieve all available books for a specific user",
            description = "Returns a list of books that belongs to specified user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The specified user ID does not exist",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorEntity.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }

    )
    @GetMapping("/users/{userId}")
    ResponseEntity<List<UserBookDto>> getAllBooksByUserId(@PathVariable Long userId);

    @Operation(
            summary = "Search for books by ISBN or Title or Author or Publisher",
            description = "Return a list of books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }

    )
    @GetMapping("/search")
    ResponseEntity<List<BookDto>> findBook(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String publisherName
    ) throws JsonProcessingException;


    @DeleteMapping
    void deleteBookFromLibrary(
           @RequestParam(name = "userId") Long userId,
           @RequestParam(name = "bookId") Long bookId
    );
}
