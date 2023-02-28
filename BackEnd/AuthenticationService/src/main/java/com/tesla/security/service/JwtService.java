package com.tesla.security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_Key = "25432A462D4A614E645266556A586E3272357538782F413F4428472B4B625065";

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token, Claims::getSubject);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		long expMillis = nowMillis + 1000 * 60 * 60;
		Date exp = new Date(expMillis);
		
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(exp)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();

	}

	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);

	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	boolean isTokenExpired(String token) {

		// TODO Auto-generated method stub
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		// TODO Auto-generated method stub
		return extractClaims(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_Key);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	 public String invalidateToken(String token) {
		    Date now = new Date();
		    return Jwts.builder()
		      .setClaims(extractAllClaims(token))
		      .setIssuedAt(now)
		      .setExpiration(now)
		      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
		      .compact();
		  }

}
