package iade.pt.backend.security;

import iade.pt.backend.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "minha_chave_secreta_super_segura_123456";
    private static final long EXPIRATION_MS = 7L * 24 * 60 * 60 * 1000;

    private final Key signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String generateToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .claim("nome", usuario.getNome())
                .claim("email", usuario.getEmail())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✨ Novo método necessário
    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
    }
}
