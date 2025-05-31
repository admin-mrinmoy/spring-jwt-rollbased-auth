package com.jwt.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

	private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public String generateToken(String username, Set<String> roles) {
		return Jwts.builder().setSubject(username).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiry
				.signWith(key).compact();
	}

	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	@SuppressWarnings("unchecked")
	public Set<String> getRolesFromToken(String token) {
		return (Set<String>) Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.get("roles");
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			// Log the exception if needed
		}
		return false;
	}
}