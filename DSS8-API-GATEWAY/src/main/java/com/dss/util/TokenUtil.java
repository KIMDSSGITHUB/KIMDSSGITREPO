package com.dss.util;

import com.dss.exception.JwtTokenMalformedException;
import com.dss.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public Jws<Claims> validateToken(String token) throws JwtTokenMalformedException, JwtTokenMissingException {

        if (!token.contains("Bearer")) {
            throw new JwtTokenMalformedException("Invalid Bearer Token");
        }
        token = StringUtils.removeStart(token, "Bearer").trim();
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (SignatureException ex) {
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenMalformedException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenMalformedException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenMalformedException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }

    }

    public Claims getClaims (final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        return validateToken(token).getBody();
    }
}
