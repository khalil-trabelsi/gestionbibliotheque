package com.isima.gestionbibliotheque.dto;

import com.isima.gestionbibliotheque.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SharedCollectionRequestDto {
    private Long collectionId;
    private Set<String> permissions;
}
