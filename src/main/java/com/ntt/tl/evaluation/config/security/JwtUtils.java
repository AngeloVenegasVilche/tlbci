package com.ntt.tl.evaluation.config.security;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	@Value("${security.keyEncript}")
	private String keyEncipt;

	@Value("${security.timeToken}")
	private String tokenExpiration;
	
	
	public String createToken(Map<String, Object> propertyUser, String user) {
		Date now = new Date();

		return Jwts.builder()
				.setClaims(propertyUser)
				.setSubject(user)
				.setIssuedAt(now)
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpiration)))
				.signWith(getSignaturekey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Boolean isTokenValid(String token) {
		try {
	        Jwts.parserBuilder()
            .setSigningKey(getSignaturekey())
            .build()
            .parseClaimsJws(token)
            .getBody();
			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public String getUserNameForToken(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public <T> T getClaim(String toke, Function<Claims, T> claimsTFunction) {
		Claims claims = extractAllClaims(toke);
		return claimsTFunction.apply(claims);
	}

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignaturekey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

	public SecretKey getSignaturekey() {
		byte[] keyBytes = Decoders.BASE64.decode(keyEncipt);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
