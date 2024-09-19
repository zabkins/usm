package pl.zarczynski.usm.configuration.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
	@Value("${security.jwt.secret-key}")
	private String secretKey;
	@Value("${security.jwt.expiration-time}")
	@Getter
	private long jwtExpirationTime;

	public String extractUsername (String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim (String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken (UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken (Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpirationTime);
	}

	private String buildToken (Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpirationTime) {
		return Jwts.builder()
				.claims().add(extraClaims).and()
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
				.signWith(getSignInKey())
				.compact();
	}

	public boolean isTokenValid (String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired (String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration (String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims (String token) {
		try {
			return Jwts.parser().verifyWith((SecretKey) getSignInKey()).build().parseSignedClaims(token).getPayload();
		} catch (JwtException e) {
			throw new RuntimeException("Invalid JWT Token");
		}
	}

	private Key getSignInKey () {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String refreshToken (String token) {
		Claims claims = extractAllClaims(token);
		return Jwts.builder()
				.claims(claims)
				.subject(claims.getSubject())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
				.signWith(getSignInKey())
				.compact();
	}
}
