package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private Book book;
    private int rating;
    private String location;
    private String status;
}
