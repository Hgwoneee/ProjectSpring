package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
	private static final String SECRET_KEY = "230823_AIOT_backend";
	private static final long EXPIRATION_TIME = 600000; // 1 hours

	public static String generateToken(String memId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", memId);
		claims.put("created", new Date());

		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
	}

	public static String getMemIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	

	public static boolean validateToken(String token, String email) {
		String tokenUsername = getMemIdFromToken(token);
		return (tokenUsername.equals(email) && !isTokenExpired(token));
	}
	 
	
	private static boolean isTokenExpired(String token) {
		Date expiration = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
		return expiration.before(new Date());
	}


}
