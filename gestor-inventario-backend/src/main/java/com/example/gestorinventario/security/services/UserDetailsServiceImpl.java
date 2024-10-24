package com.example.gestorinventario.security.services;

import com.example.gestorinventario.entity.Usuario;
import com.example.gestorinventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Implementación de UserDetailsService que carga los detalles de usuario
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección de dependencias del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Sobrescribe el método para cargar el usuario por nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca al usuario en la base de datos por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username);

        // Si no encuentra el usuario, lanza una excepción
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
        }

        // Si lo encuentra, crea un objeto CustomUserDetails con los datos del usuario y lo retorna
        return new CustomUserDetails(usuario);
    }
}
