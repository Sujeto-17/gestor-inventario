package com.example.gestorinventario.security.services;

import com.example.gestorinventario.entity.Rol;
import com.example.gestorinventario.entity.Usuario;
import com.example.gestorinventario.security.repository.RolRepository;
import com.example.gestorinventario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Marcado como un servicio de Spring
@Service
public class UsuarioService {

    // Inyección de dependencias del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Inyección de dependencias del repositorio de roles
    @Autowired
    private RolRepository rolRepository;

    // Inyección de dependencias del codificador de contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para buscar un usuario por nombre de usuario
    public Usuario buscarPorNombre(String username) {
        // Usa el repositorio para encontrar al usuario por nombre de usuario
        return usuarioRepository.findByUsername(username);
    }

    // Método para guardar un usuario en la base de datos
    public Usuario guardarUsuario(Usuario usuario) {
        // Establece el nombre de usuario (opcional, ya que ya viene asignado)
        usuario.setUsername(usuario.getUsername());

        // Establece los roles del usuario (ya viene asignado en la entidad Usuario)
        usuario.setRoles(usuario.getRoles());

        // Codifica la contraseña del usuario antes de guardarlo
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guarda el usuario en la base de datos y obtiene el objeto guardado
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Itera sobre los roles del usuario y agrega el usuario guardado a la lista de usuarios en cada rol
        for (Rol rol : usuario.getRoles()) {
            rol.getUsuarios().add(usuarioGuardado); // Asocia el usuario con los roles
        }

        // Retorna el usuario guardado
        return usuarioGuardado;
    }
}