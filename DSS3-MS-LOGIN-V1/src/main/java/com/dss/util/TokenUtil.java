package com.dss.util;

import com.dss.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class TokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMin;

    public String generateToken(User user) {

        Calendar now = Calendar.getInstance();
        Date issueTime = now.getTime();
        now.add(Calendar.MINUTE, jwtExpirationInMin);
        Date expiryTime = now.getTime();

        return Jwts.builder()
                .setIssuer("User Service")
                .setSubject(user.getId().toString())
                .setIssuedAt(issueTime)
                .setExpiration(expiryTime)
                .claim("user", user.getEmail())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
