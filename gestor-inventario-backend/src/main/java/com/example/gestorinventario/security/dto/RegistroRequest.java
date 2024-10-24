package com.example.gestorinventario.security.dto;

import com.example.gestorinventario.emuns.RolEnum;
import lombok.Data;

import java.util.Set;

@Data
public class RegistroRequest {
    private String username;
    private String password;
    private Set<RolEnum> roles;
}
