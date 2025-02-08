package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Book;
import com.isima.gestionbibliotheque.model.Tag;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String isbn;
    private int rating;
    private String location;
    private String status;
}
