package com.example.sns.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;

    public JwtTokenUtils(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }
    // JWT 발급 메소드
    public String generateToken(UserDetails userDetails) {
        Claims jwtClaims = Jwts.claims()
                // 사용자 정보 등록
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(7200))); //2h

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }
    // jwt 유효성 검사 메소드
    public boolean validate(String token) {
        // parseClaimsJws를 사용해 정보를 해석
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt");
//            e.printStackTrace();
        }
        return false;
    }
    // JWT를 인자로 받고, 그 JWT를 해석해 사용자 정보를 회수하는 메소드
    public Claims parseClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
