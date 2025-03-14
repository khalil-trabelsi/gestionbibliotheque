package com.isima.gestionbibliotheque.controller;

import com.isima.gestionbibliotheque.Exception.BadRequestException;
import com.isima.gestionbibliotheque.Exception.ErrorCode;
import com.isima.gestionbibliotheque.controller.api.CollectionApiDocs;
import com.isima.gestionbibliotheque.dto.*;
import com.isima.gestionbibliotheque.service.AccessKeyService;
import com.isima.gestionbibliotheque.service.CollectionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CollectionController implements CollectionApiDocs {

    private final CollectionService collectionService;
    private final AccessKeyService accessKeyService;

    @Autowired
    public CollectionController(
            CollectionService collectionService,
            AccessKeyService accessKeyService
    ) {
        this.collectionService = collectionService;
        this.accessKeyService = accessKeyService;
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getAllCollections() {
        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @Override
    public ResponseEntity<CollectionDto> getCollectionById(@PathVariable Long collectionId,
    @RequestParam(required = false) String key) {
        return ResponseEntity.ok(collectionService.getCollectionById(collectionId, key));
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getAllCollectionsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(collectionService.getAllCollectionsByUserId(userId));
    }

    @Override
    public ResponseEntity<List<CollectionDto>> getAllCollectionsByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(collectionService.getAllCollectionsByBookId(bookId));
    }

    @Override
    public ResponseEntity<CollectionDto> createCollection(
            @Valid @RequestBody CreateCollectionDto dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        List<String> errors = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError: bindingResult.getFieldErrors()) {
                errors.add(fieldError.getDefaultMessage());
            }
            throw new BadRequestException("Invalid input data or/and Missing required fields", ErrorCode.COLLECTION_NOT_VALID, errors);
        }

        CollectionDto collectionDto = collectionService.createCollection(dto, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(collectionDto);
    }


    @Override
    public ResponseEntity<SharedCollectionResponseDto> shareCollection(HttpServletRequest request, @RequestBody SharedCollectionRequestDto dto) {
        final String SERVER_URI = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        SharedCollectionResponseDto response = accessKeyService.createAccessKey(dto.getCollectionId(), dto.getPermissions(), SERVER_URI);
        return ResponseEntity.ok(response);
    }

    @Override
    public CollectionDto updateCollection(
            @RequestBody UpdateCollectionDto updateCollectionDto,
            @PathVariable Long collectionId,
            @RequestParam(required = false) String key
            ) {
        return collectionService.updateCollection(updateCollectionDto, collectionId, key);
    }
    @Override
    public ResponseEntity<Map<String, String>> deleteCollection(@PathVariable Long collectionId) {
        collectionService.deleteCollection(collectionId);
        return ResponseEntity.ok(Map.of("success", "Collection deleted successfully"));
    }

    @Override
    public ResponseEntity<CollectionDto> addBookToCollection(
            @RequestBody BookCollectionRequest bookCollectionRequest
    ) {
        CollectionDto collection = collectionService.addBookToCollection(bookCollectionRequest.getCollectionId(), bookCollectionRequest.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(collection);
    }
}
