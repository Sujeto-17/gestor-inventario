package com.example.gestorinventario.security.controller;

import com.example.gestorinventario.emuns.RolEnum;
import com.example.gestorinventario.entity.Rol;
import com.example.gestorinventario.entity.Usuario;
import com.example.gestorinventario.security.dto.AuthRequest;
import com.example.gestorinventario.security.dto.AuthResponse;
import com.example.gestorinventario.security.dto.RegistroRequest;
import com.example.gestorinventario.security.repository.RolRepository;
import com.example.gestorinventario.security.services.JwtUtil;
import com.example.gestorinventario.security.services.UserDetailsServiceImpl;
import com.example.gestorinventario.security.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody AuthRequest authRequest){
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest registroRequest){
        if(usuarioService.buscarPorNombre(registroRequest.getUsername()) != null){
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe en el sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registroRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registroRequest.getUsername()));

        Set<Rol> roles = new HashSet<>();

        if(registroRequest.getRoles() != null){
            for (RolEnum rolEnum: registroRequest.getRoles()){
                Rol rolObj =rolRepository.findByNombre(rolEnum.name());
                if(rolObj != null){
                    roles.add(rolObj);
                }
            }

            usuario.setRoles(roles);
        }

        if(roles.isEmpty()){
            Rol userRole = rolRepository.findByNombre(RolEnum.ROLE_USER.getRol());
            roles.add(userRole);
            usuario.setRoles(roles);
        }

        usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok().body("{\"message\": \"UsuarioRegistrado exitosamente\"}");
    }
}
