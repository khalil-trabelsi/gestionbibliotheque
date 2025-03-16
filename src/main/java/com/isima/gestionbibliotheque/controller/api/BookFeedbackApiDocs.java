package com.isima.gestionbibliotheque.controller.api;

import com.isima.gestionbibliotheque.Exception.EntityNotFoundException;
import com.isima.gestionbibliotheque.Exception.ErrorEntity;
import com.isima.gestionbibliotheque.dto.BookFeedbackDto;
import com.isima.gestionbibliotheque.dto.FeedbackRequest;
import com.isima.gestionbibliotheque.dto.UpdateFeedbackRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Book Feedback", description = "API for managing book feedbacks")
@RequestMapping("/api/bookFeedback")
public interface BookFeedbackApiDocs {

    @Operation(
            summary = "Retrieve book's feedbacks for a given book ID",
            description = "Returns a list of book's feedbacks",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = BookFeedbackDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Book not found. The specified book ID does not exist", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping("/book/{bookId}")
    ResponseEntity<List<BookFeedbackDto>> getAllBookFeedbackByBookId(@PathVariable Long bookId);

    @Operation(
            summary = "Retrieve a book's feedback based on its id",
            description = "Returns a feedback",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookFeedbackDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Book not found. The specified feedback ID does not exist", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping("/{feedbackId}")
    ResponseEntity<BookFeedbackDto> getBookFeedbackById(@PathVariable Long feedbackId);

    @Operation(
            summary = "Retrieve a book's feedback based for a given book & a given user",
            description = "Returns a feedback",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = BookFeedbackDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Book not found.", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping
    ResponseEntity<BookFeedbackDto> getBookFeedbackByBookIdAndUserId(
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "userId") Long userId
    );

    @Operation(
            summary = "Create a feedback for a given book.",
            description = "Creates a new feedback",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Feedback successfully created", content = @Content(schema = @Schema(implementation = BookFeedbackDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. Invalid input data or missing required fields.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized. User is not authenticated.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @PostMapping
    ResponseEntity<BookFeedbackDto> createBookFeedback(@RequestBody FeedbackRequest request);

    @Operation(
            summary = "Update a feedback for a given book.",
            description = "Updates an existing feedback based on the provided feedback ID.",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Feedback successfully updated", content = @Content(schema = @Schema(implementation = BookFeedbackDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. Invalid input data or missing required fields.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized. User is not authenticated.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @PutMapping("/{feedbackId}")
    ResponseEntity<BookFeedbackDto> updateBookFeedback(@PathVariable Long feedbackId, @RequestBody UpdateFeedbackRequest request);

    @Operation(
            summary = "Delete a feedback.",
            description = "Deletes a feedback based on the provided feedback ID. Only authorized users can perform this action.",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Feedback successfully deleted", content = @Content(schema = @Schema(implementation = void.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized. User is not authenticated.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. The user does not have permission to modify this feedback.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @DeleteMapping("/{feedbackId}")
    void deleteBookFeedback(@PathVariable Long feedbackId);
}
