package com.isima.gestionbibliotheque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequest {
    private String isbn;
    private String title;
    private String authorName;
    private String publisher;
}
