package vd.excel_demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vd.excel_demo.models.User;

import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000*60*30;
    private static final String SCOPES = "scopes";
    private static final String SIGNING_KEY = "excel-demo-project";

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public String generateToken(User user) {
        return doGenerateToken(user.getEmail(), user.getAuthority().getRole());
    }

    boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername())
                && !this.isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String doGenerateToken(String subject, String authority) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(SCOPES, Collections.singletonList(authority));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    private Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
