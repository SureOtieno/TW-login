package com.example.tendawaks;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class TendawaksApplication {

	private static final Logger logger = Logger.getLogger(TendawaksApplication.class.getName());
	private static final int MIN_SECRET_LENGTH = 32; // 256 bits

	@Value("${jwt.secret:#{null}}")
	private String jwtSecret;

	@Value("${jwt.expiration:#{0}}")
	private long expiration;

	public static void main(String[] args) {
		SpringApplication.run(TendawaksApplication.class, args);
	}

	@PostConstruct
	public void validateJwtConfig() {

		try {
			// Validate secret exists and is properly encoded
			if (jwtSecret == null || jwtSecret.isBlank()) {
				throw new IllegalStateException("JWT secret is not configured in application.properties");
			}

			// Verify secret length after Base64 decoding
			byte[] decodedSecret;
			try {
				decodedSecret = Base64.getDecoder().decode(jwtSecret);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("JWT secret is not valid Base64: " + e.getMessage());
			}

			if (decodedSecret.length < MIN_SECRET_LENGTH) {
				throw new IllegalStateException(String.format(
						"JWT secret must be at least %d bytes (256 bits) after Base64 decoding. Current length: %d bytes",
						MIN_SECRET_LENGTH, decodedSecret.length));
			}

			// Validate expiration time
			if (expiration <= 0) {
				throw new IllegalStateException("JWT expiration must be positive (in milliseconds)");
			}

			logger.log(Level.INFO, "✅ JWT Configuration Validated - Secret: [REDACTED], Expiration: {0}ms (~{1} minutes)",
					new Object[]{expiration, expiration / 60000});

		} catch (Exception e) {
			logger.log(Level.SEVERE, "❌ FATAL: Invalid JWT Configuration - " + e.getMessage());
			throw new RuntimeException("Failed to validate JWT configuration", e);
		}
	}

}