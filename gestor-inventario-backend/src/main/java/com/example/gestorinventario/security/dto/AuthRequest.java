package com.example.gestorinventario.security.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthRequest {

    private String username;
    private String password;
}
