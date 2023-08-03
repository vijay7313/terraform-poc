package com.terraform.configure.security.jwtmanager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenUtil  {
	  
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	 public String extractEmail(String token) {
	        return extractClaim(token, claims -> claims.get("email", String.class));
	    }

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }


	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractEmail(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String generateToken(String userName,String email,String role) {
		Map<String, Object> claims = new HashMap<>();
		 claims.put("role", role);
		 claims.put("email", email);
		return createToken(claims, userName);
	}

	private String createToken(Map<String, Object> claims, String userName) {

		return Jwts.builder().
				setClaims(claims).
				setSubject(userName).
				setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2880))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode("bpk/yxNMJFz/CSvh3lsCjxMTn2rFZG7sSmdXqr5adslQuoNNeHfVy/Op9oKv9wgG");
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUserRole(String token) {
	    return extractClaim(token, claims -> claims.get("role", String.class));
	}
	
}
