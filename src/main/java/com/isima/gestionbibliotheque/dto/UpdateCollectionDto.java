package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCollectionDto {
    private String name;
    private String description;
    @JsonProperty("isShareable")
    private boolean isShareable;
    private List<Long> booksId;

}
