package com.isima.gestionbibliotheque.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isima.gestionbibliotheque.model.Collection;
import com.isima.gestionbibliotheque.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Date createdAt;
    private LocalDate birthDate;

    @JsonIgnoreProperties({"user"})
    private List<Collection> collections = new ArrayList<>();

    @JsonIgnore
    private String password;


    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .birthDate(user.getBirthDate())
                .collections(user.getCollections().stream()
                        .filter(Collection::isShareable)
                        .toList())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
