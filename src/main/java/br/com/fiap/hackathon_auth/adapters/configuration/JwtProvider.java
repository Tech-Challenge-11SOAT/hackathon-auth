package br.com.fiap.hackathon_auth.adapters.configuration;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.fiap.hackathon_auth.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtProvider {

	private static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS256;
	private final JwtProperties jwtProperties;
	private SecretKey key;

	public JwtProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(User user) {
		// List<String> roles = Optional.ofNullable(user.getRoles())
		// .orElse(Collections.emptyList())
		// .stream()
		// .map(role -> role.getNome())
		// .toList();

		return Jwts.builder()
				.subject(user.getEmail())
				.claim("idUser", user.getId())
				// .claim("roles", roles)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
				.signWith(key, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String generateRefreshToken(User user) {
		return Jwts.builder()
				.subject(user.getEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpiration()))
				.signWith(key, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String getCurrentUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("Usuário não autenticado");
		}
		return authentication.getName();
	}

	public String getEmailFromToken(String token) {
		return this.parseClaims(token).getPayload().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			this.parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Claims getClaims(String token) {
		return this.parseClaims(token).getPayload();
	}

	public UUID getUserIdFromToken(String token) {
		Object userIdClaim = getClaims(token).get("idUser");
		if (userIdClaim == null) {
			throw new RuntimeException("Token sem idUser");
		}
		return UUID.fromString(userIdClaim.toString());
	}

	public Duration getTokenTtl(String token) {
		Date expiration = getClaims(token).getExpiration();
		long seconds = Duration.between(Instant.now(), expiration.toInstant()).getSeconds();
		return Duration.ofSeconds(Math.max(1, seconds));
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}

	public String extractToken(String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		throw new RuntimeException("Token inválido");
	}

}
