package com.isima.gestionbibliotheque.controller.api;

import com.isima.gestionbibliotheque.Exception.ErrorEntity;
import com.isima.gestionbibliotheque.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Collections", description = "Endpoints to manage collections")
@RequestMapping("/api/collections")
public interface CollectionApi {

    @Operation(
            summary = "Retrieve the list of collections",
            description = "Returns a list of all available collections",
            responses = {
                @ApiResponse(
                        responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CollectionDto.class))
                ),
                @ApiResponse(
                        responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                )
            }
    )
    @GetMapping
    ResponseEntity<List<CollectionDto>> getAllCollections();

    @Operation(
            summary = "Retrieve a collection based on its id",
            description = "Returns a collection",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CollectionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied. The user does not have permission to access this collection.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping("/{collectionId}")
    ResponseEntity<CollectionDto> getCollectionById(@PathVariable Long collectionId, @RequestParam(required = false) String key);

    @Operation(
            summary = "Retrieve all collections for a specific user",
            description = " a list of collections that belong to the specified user",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CollectionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied. The user does not have  permission to access these collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The specified user ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping("/users/{userId}")
    ResponseEntity<List<CollectionDto>> getAllCollectionsByUserId(@PathVariable Long userId);

    @Operation(
            summary = "Retrieve all collections for a specific user book",
            description = "Returns a list of collections associated with the specified user book.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CollectionDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Access Denied. The user does not have  permission to access these collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The specified user book ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @GetMapping("/userBooks/{userBookId}")
    ResponseEntity<List<CollectionDto>> getAllCollectionsByUserBookId(@PathVariable Long userBookId);

    @Operation(
            summary = "Create a new collection",
            description = "Creates a new collection for the authenticated user.",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "201", description = "Collection successfully created.", content = @Content(schema = @Schema(implementation = CollectionDto.class))
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
                            responseCode = "403", description = "Forbidden. User does not have permission to perform this action.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CollectionDto> createCollection(
            @Valid @RequestBody CreateCollectionDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @PostMapping("/share")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Share a collection",
            description = "Generates an access key to share a collection with specific permissions.Returns a response containing the generated access key and sharing details..",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Collection successfully shared. Returns the access key and details.",
                            content = @Content(schema = @Schema(implementation = SharedCollectionResponseDto.class))
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
                            responseCode = "403", description = "Forbidden. User does not have permission to perform this action.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. The specified collection ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    ResponseEntity<SharedCollectionResponseDto> shareCollection(HttpServletRequest request, @RequestBody SharedCollectionRequestDto dto);



    @PutMapping("/{collectionId}")
    @Operation(
            summary = "Update an existing collection",
            description = "Updates the details of a collection based on the provided collection ID. An optional access key can be provided for authorization.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Collection successfully shared. Returns the access key and details.",
                            content = @Content(schema = @Schema(implementation = SharedCollectionResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. Invalid input data or missing required fields.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden. User does not have permission to perform this action.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. The specified collection ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    CollectionDto updateCollection(
            @RequestBody CreateCollectionDto createCollectionDto,
            @PathVariable Long collectionId,
            @RequestParam(required = false) String key
    );

    @Operation(
            summary = "Delete a collection",
            description = "Deletes a collection based on the provided collection ID. Only authorized users can perform this action.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Collection successfully deleted.",
                            content = @Content(schema = @Schema(implementation = Map.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden. User does not have permission to delete this collection.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. The specified collection ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error. An unexpected error occurred.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @DeleteMapping("/{collectionId}")
    ResponseEntity<Map<String, String>> deleteCollection(@PathVariable Long collectionId);


    @Operation(
            summary = "Add a book to a collection",
            description = "Adds a book to the specified collection. If no link exists between the user and the book, a new link is created.",
            responses = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book successfully added to the collection.",
                    content = @Content(schema = @Schema(implementation = CollectionDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. You do not have permission to modify this collection.",
                    content = @Content(schema = @Schema(implementation = ErrorEntity.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found. The specified collection ID or book ID does not exist.",
                    content = @Content(schema = @Schema(implementation = ErrorEntity.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. An unexpected error occurred while adding the book to the collection.",
                    content = @Content(schema = @Schema(implementation = ErrorEntity.class))
            )}
    )
    @PostMapping("/add_book_collection")
    ResponseEntity<CollectionDto> addBookToCollection(@RequestBody BookCollectionRequest bookCollectionRequest);

}
