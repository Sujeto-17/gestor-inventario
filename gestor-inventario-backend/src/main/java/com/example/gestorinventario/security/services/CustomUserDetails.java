package com.example.gestorinventario.security.services;

import com.example.gestorinventario.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    // Atributos para almacenar el nombre de usuario, contraseña y roles (autoridades) del usuario
    private String username;
    private String password;
    private Set<GrantedAuthority> authorities;

    // Constructor que toma un objeto Usuario y lo convierte en un objeto CustomUserDetails
    public CustomUserDetails(Usuario usuario) {
        // Asigna el nombre de usuario
        this.username = usuario.getUsername();
        // Asigna la contraseña
        this.password = usuario.getPassword();
        // Convierte los roles de Usuario en GrantedAuthority para Spring Security
        this.authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre())) // Prefija el rol con "ROLE_"
                .collect(Collectors.toSet()); // Convierte el stream en un Set
    }

    // Retorna la lista de roles (autoridades) del usuario
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Retorna la contraseña del usuario
    @Override
    public String getPassword() {
        return password;
    }

    // Retorna el nombre de usuario
    @Override
    public String getUsername() {
        return username;
    }

    // Verifica si la cuenta ha expirado (en este caso, siempre retorna true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Verifica si la cuenta está bloqueada (aquí, siempre retorna true indicando que no está bloqueada)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Verifica si las credenciales han expirado (retorna true indicando que no han expirado)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Verifica si la cuenta está habilitada (siempre retorna true)
    @Override
    public boolean isEnabled() {
        return true;
    }
}