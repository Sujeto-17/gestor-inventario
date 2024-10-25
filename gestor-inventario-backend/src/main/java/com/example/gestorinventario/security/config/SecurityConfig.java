package com.example.gestorinventario.security.config;

import com.example.gestorinventario.security.filter.JwtFilter;
import com.example.gestorinventario.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Anotación que indica que esta es una clase de configuración
@EnableWebSecurity // Habilita la configuración de seguridad en Spring
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Inyección de dependencia del filtro JwtFilter para manejar autenticación con JWT

    // Configuración del filtro de seguridad para manejar rutas y autenticación
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Permite acceso sin autenticación a las rutas que comienzan con "/api/auth/"
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                )
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF ya que se maneja autenticación sin estado (stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la sesión como sin estado (no guarda sesión en servidor)
                .authenticationProvider(authenticationProvider()) // Especifica el proveedor de autenticación personalizado
                .build(); // Construye la cadena de filtros de seguridad configurada
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        // Define un proveedor de autenticación que se encarga de validar credenciales y autenticación
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Establece el servicio de usuarios personalizado
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Define el encoder de contraseñas
        return authenticationProvider; // Devuelve el proveedor de autenticación configurado
    }

    @Bean
    public UserDetailsService userDetailsService(){
        // Define el servicio de carga de usuarios personalizados
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // Define un codificador de contraseñas seguro (BCrypt) para almacenar y verificar contraseñas cifradas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Proporciona el gestor de autenticación principal que integra el mecanismo de autenticación configurado
        return configuration.getAuthenticationManager();
    }
}