package com.isima.gestionbibliotheque.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookRequest {
    private String isbn;
    private int rating;
    private String location;
    private String status;
}
