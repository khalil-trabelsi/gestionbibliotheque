package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCollectionDto {
    @NotBlank(message = "Le champ 'nom' est obligatoire")
    private String name;
    private String description;
    @JsonProperty("isShareable")
    private boolean isShareable;
}
