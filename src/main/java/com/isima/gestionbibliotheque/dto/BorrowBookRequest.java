package com.isima.gestionbibliotheque.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowBookRequest {

    @NotNull
    private Long userBookId;

    @NotNull
    private Integer loanDurationInDays;

}
