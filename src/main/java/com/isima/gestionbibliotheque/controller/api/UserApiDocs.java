package com.isima.gestionbibliotheque.controller.api;

import com.isima.gestionbibliotheque.Exception.ErrorEntity;
import com.isima.gestionbibliotheque.dto.BookDto;
import com.isima.gestionbibliotheque.dto.CollectionDto;
import com.isima.gestionbibliotheque.dto.UpdateUserDto;
import com.isima.gestionbibliotheque.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Users", description = "API for managing users")
@RequestMapping("/api/users")
public interface UserApiDocs {

    @Operation(
            summary = "Retrieve all users",
            description = "Returns a list of users",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<UserDto>> getAllUsers();

    @Operation(
            summary = "Retrieve a user based on its username",
            description = "Returns a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The specified username does not exist",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorEntity.class))
                            )
                    )
            }
    )
    @GetMapping("/username/{username}")
    ResponseEntity<UserDto> getUserByUsername(@PathVariable String username);

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve a user based on its id",
            description = "Returns a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found. The specified user ID does not exist",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorEntity.class))
                            )
                    )
            }
    )
    ResponseEntity<UserDto> getUserById(@PathVariable Long id);


    @Operation(
            summary = "Update an existing user",
            description = "Updates the details of a user based on the provided user ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "User successfully updated.",
                            content = @Content(schema = @Schema(implementation = CollectionDto.class))
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
                            description = "Not Found. The specified user ID does not exist.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "500", description = "Internal Server Error. An unexpected error occurred while retrieving collections.",
                            content = @Content(schema = @Schema(implementation = ErrorEntity.class))
                    )
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto request);

}
