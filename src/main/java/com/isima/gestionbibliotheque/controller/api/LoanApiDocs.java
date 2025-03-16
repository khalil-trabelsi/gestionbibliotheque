package com.isima.gestionbibliotheque.controller.api;

import com.isima.gestionbibliotheque.dto.LoanDto;
import com.isima.gestionbibliotheque.dto.BorrowBookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Loan Management", description = "API for managing book loans.")
@RequestMapping("/api/loans")
public interface LoanApiDocs {


    @Operation(
            summary = "Get loan history of the authenticated borrower",
            description = "Retrieve all loan transactions made by the authenticated borrower",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of loan history"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )

    @GetMapping("/history/borrowers/{borrowerId}")
    ResponseEntity<List<LoanDto>> getBorrowerLoanHistory(@PathVariable Long borrowerId);

    @Operation(
            summary = "Get all borrowed books of a borrower",
            description = "Retrieve all books that a specific borrower has borrowed",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of borrowed books")
            }
    )
    @GetMapping("/history/borrowers/{borrowerId}/borrowed_books")
    ResponseEntity<List<LoanDto>> getAllBorrowedBooks(
            @Parameter(description = "ID of the borrower", required = true) @PathVariable Long borrowerId
    );

    @Operation(
            summary = "Get all returned books of a borrower",
            description = "Retrieve all books that a specific borrower has returned",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of returned books")
            }
    )
    @GetMapping("/history/borrowers/{borrowerId}/returned_books")
    ResponseEntity<List<LoanDto>> getAllReturnedBooks(
            @Parameter(description = "ID of the borrower", required = true) @PathVariable Long borrowerId
    );

    @Operation(
            summary = "Get loan history of a book for a user",
            description = "Retrieve all loan transactions of a specific book for a specific user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of loan history for the book")
            }
    )
    @GetMapping("/history")
    ResponseEntity<List<LoanDto>> getBookLoanHistory(
            @Parameter(description = "ID of the book", required = true) @RequestParam(name = "bookId") Long bookId,
            @Parameter(description = "ID of the user", required = true) @RequestParam(name = "userId") Long userId
    );

    @Operation(
            summary = "Borrow a book",
            description = "Allow a user to borrow a book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully borrowed the book"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    ResponseEntity<LoanDto> borrowBook(
            @Parameter(description = "Request to borrow a book", required = true) @Valid @RequestBody BorrowBookRequest borrowBookRequest,
            BindingResult bindingResult
    );

    @Operation(
            summary = "Return a borrowed book",
            description = "Allow a user to return a borrowed book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned the book")
            }
    )
    @PutMapping("/{loanId}/return_book")
    ResponseEntity<LoanDto> returnBook(
            @Parameter(description = "ID of the loan", required = true) @PathVariable Long loanId
    );
}
