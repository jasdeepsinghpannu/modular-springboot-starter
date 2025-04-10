package com.space.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

  // 24 hours token validity
  private static final long JWT_EXPIRATION_MS = 24 * 60 * 60 * 1000;

  // Replace with env-based secret later
  private static final String JWT_SECRET_KEY = "12345678901234567890123456789012"; // Must be at
                                                                                   // least 256-bit
                                                                                   // (32 chars)

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
  }

  public String extractEmail(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
        .getBody();
  }

  public String generateToken(String email) {
    return Jwts.builder().setSubject(email).setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
  }

  public boolean isTokenValid(String token, String email) {
    String extractedEmail = extractEmail(token);
    return (email.equals(extractedEmail) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
