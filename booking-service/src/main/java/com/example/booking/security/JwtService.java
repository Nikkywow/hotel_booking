package com.example.booking.security;

import com.example.booking.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String secret;

    public JwtService(@Value("${security.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(String username, Role role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("roles", "ROLE_" + role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
