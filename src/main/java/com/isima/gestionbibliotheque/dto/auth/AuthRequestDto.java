package com.isima.gestionbibliotheque.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    private String username;
    private String password;
}
