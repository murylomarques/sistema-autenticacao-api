package br.com.murylomarques.auth.sistema_autenticacao_api.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    // Chave secreta para assinar o token
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(Authentication authentication) {
        // Pega os detalhes do usuário que foi autenticado
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // Constrói o token JWT
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Define o "dono" do token
                .setIssuedAt(new Date()) // Data de criação
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Assina com o segredo
                .compact(); // Constrói a string do token
    }

    // (Futuramente, adicionaremos métodos para validar o token aqui)
}