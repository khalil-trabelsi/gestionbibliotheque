package com.isima.gestionbibliotheque.dto.auth;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
