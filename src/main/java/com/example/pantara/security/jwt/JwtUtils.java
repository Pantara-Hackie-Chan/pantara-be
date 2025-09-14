package com.example.pantara.security.jwt;

import com.example.pantara.security.services.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret; // Base64-encoded secret (min 256-bit untuk HS256)

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .claim("email", userPrincipal.getEmail())
                .claim("username", userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                // Pada 0.12.x, algoritma diinfer dari tipe key; tidak perlu SignatureAlgorithm.HS256
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        // jwtSecret harus Base64; contoh generate: openssl rand -base64 32
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            // Akan melempar JwtException jika invalid/expired/unsupported, dll.
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException meliputi malformed, unsupported, signature invalid, dll.
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    public Date getIssuedAtDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getIssuedAt();
    }

    public boolean isTokenValidForUser(String token, Instant userTokenValidAfter) {
        try {
            Date issuedAt = getIssuedAtDateFromToken(token);
            return issuedAt != null && issuedAt.toInstant().isAfter(userTokenValidAfter);
        } catch (Exception e) {
            return false;
        }
    }
}
