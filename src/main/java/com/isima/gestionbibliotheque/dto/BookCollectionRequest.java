package com.isima.gestionbibliotheque.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCollectionRequest {
    private Long collectionId;
    private Long bookId;
}
