package com.isima.gestionbibliotheque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowBookRequest {

    private Long userBookId;
    private LocalDate expectedReturnDate;

}
