package app.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;

public class JwtProvider {

    private String secret = "f4h9t87t3g473HGufuJ8fFHU4j39j48fmu948cx48cu2j9fj";
    private Long jwtExpirationInMillis = 9000000L;

    public String generateJWT(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) throws SignatureException {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt);
    }

    public boolean validateToken(String jwt) {
        try {
            parseJWT(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }
}
