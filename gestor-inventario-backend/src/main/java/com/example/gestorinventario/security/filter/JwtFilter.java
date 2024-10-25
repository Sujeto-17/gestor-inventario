package com.example.gestorinventario.security.filter;

import com.example.gestorinventario.security.services.JwtUtil;
import com.example.gestorinventario.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter { // Filtro personalizado que se ejecuta una vez por solicitud

    @Autowired
    private JwtUtil jwtUtil; // Inyección de dependencia para JwtUtil, que maneja la generación y validación de tokens JWT
    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Inyección de dependencia para obtener detalles del usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtiene el valor del encabezado "Authorization" de la solicitud
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null; // Variable para almacenar el nombre de usuario extraído del token
        String jwt = null; // Variable para almacenar el token JWT

        // Verifica que el encabezado "Authorization" no esté vacío y que comience con "Bearer "
        if(StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")){
            // Extrae el token JWT desde el encabezado (elimina la palabra "Bearer ")
            jwt  = authorizationHeader.substring(7);
            // Extrae el nombre de usuario del token usando jwtUtil
            username = jwtUtil.extractUsername(jwt);
        }

        // Si se obtuvo un nombre de usuario y no hay autenticación en el contexto de seguridad actual
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Carga los detalles del usuario usando el nombre de usuario extraído
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valida el token usando el nombre de usuario y los detalles del token
            if(jwtUtil.validateToken(jwt,userDetails)){
                // Si el token es válido, crea un objeto de autenticación para el usuario
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                // Establece la autenticación en el contexto de seguridad de la aplicación
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continúa el procesamiento de la solicitud, pasando el control al siguiente filtro
        filterChain.doFilter(request,response);
    }
}