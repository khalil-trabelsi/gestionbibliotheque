package com.isima.gestionbibliotheque.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    private String username;
    private String password;
}
