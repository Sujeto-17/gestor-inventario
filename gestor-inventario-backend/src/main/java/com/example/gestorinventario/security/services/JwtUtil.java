package com.example.gestorinventario.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Duración del token, se obtiene del archivo de configuración (por ejemplo, application.properties)
    @Value("${jwt.expiration}")
    private long expiration;

    // Llave secreta utilizada para firmar el token JWT
    private Key secretKey;

    // Método que inicializa la clave secreta al momento de cargar el componente
    @PostConstruct
    public void init(){
        // Crea un array de bytes de tamaño 64 para la clave secreta
        byte[] apiSecretBytes = new byte[64];
        // Llena el array de bytes con valores aleatorios para aumentar la seguridad
        new SecureRandom().nextBytes(apiSecretBytes);
        // Genera una clave HMAC-SHA utilizando el array de bytes creado
        secretKey = Keys.hmacShaKeyFor(apiSecretBytes);
    }

    // Genera el token JWT usando los detalles del usuario
    public String generateToken(UserDetails userDetails) {
        // Mapa para almacenar los claims (información adicional del token)
        Map<String, Object> claims = new HashMap<>();
        // Crea el token llamando al método createToken
        return createToken(claims, userDetails.getUsername());
    }

    // Método para construir y firmar el token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) // Añade los claims al token
                .setSubject(subject) // Añade el "subject" (nombre del usuario o identificador)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración
                .signWith(Keys.hmacShaKeyFor(secretKey.getEncoded())) // Firma el token usando la clave secreta
                .compact(); // Compacta el token en una cadena
    }

    // Método genérico para extraer una reclamación específica del token (usando una función `claimsResolver`)
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        // Extrae todas las reclamaciones y aplica la función recibida para obtener un valor específico
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todas las reclamaciones (claims) del token
    private Claims extractAllClaims(String token) {
        // Verifica y decodifica el token utilizando la clave secreta
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getEncoded())) // Clave de firma
                .build() // Construye el parser
                .parseClaimsJws(token) // Parsea el token JWT firmado
                .getBody(); // Obtiene el cuerpo de las reclamaciones
    }

    // Extrae el nombre de usuario (subject) del token
    public String extractUsername(String token) {
        // Llama a `extractClaims` con la reclamación `getSubject` para obtener el nombre de usuario
        return extractClaims(token, Claims::getSubject);
    }

    // Extrae la fecha de expiración del token
    public Date extractExpiration(String token) {
        // Llama a `extractClaims` con la reclamación `getExpiration` para obtener la fecha de expiración
        return extractClaims(token, Claims::getExpiration);
    }

    // Verifica si el token ha expirado comparando la fecha de expiración con la fecha actual
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Valida que el token sea correcto para el usuario y que no esté expirado
    public boolean validateToken(String token, UserDetails userDetails) {
        // Extrae el nombre de usuario del token y lo compara con el nombre de usuario de `userDetails`
        final String username = extractUsername(token);
        // Verifica que el nombre de usuario coincida y que el token no haya expirado
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}