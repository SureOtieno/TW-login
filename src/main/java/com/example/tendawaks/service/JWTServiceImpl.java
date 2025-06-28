package com.example.tendawaks.service;

import com.example.tendawaks.model.Users;
import com.example.tendawaks.model.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTServiceImpl.class);
    private static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;
    private static final int CLOCK_SKEW_SECONDS = 30;

    private final UserRepository userRepo;
    private SecretKey secretKey;
    private final long jwtExpiration;
    private final String jwtSecret;

    public JWTServiceImpl(UserRepository userRepo,
                          @Value("${jwt.secret}") String jwtSecret,
                          @Value("${jwt.expiration}") long expiration) {
        this.userRepo = userRepo;
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = expiration;
    }

    @PostConstruct
    public void init() {
        this.secretKey = decodeSecret(jwtSecret);
        logger.info("✅ JWTService initialized. Expiration: {} ms", jwtExpiration);
        logger.info("🔐 JWT Secret length (Base64): {}", jwtSecret.length());
        logger.info("🔐 Decoded secret (hex): {}", bytesToHex(secretKey.getEncoded()));
    }

    @Override
    public String generateToken(Users user) {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, Users user) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(generateExpiryDate())
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .compact();
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isValidToken(String token, UserDetails userDetails) {
        try {
            String username = extractUserName(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("❌ Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Date generateExpiryDate() {
        return new Date(System.currentTimeMillis() + jwtExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .clockSkewSeconds(CLOCK_SKEW_SECONDS)
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey decodeSecret(String base64Key) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("JWT secret must be valid Base64", e);
        }

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must decode to at least 32 bytes");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
