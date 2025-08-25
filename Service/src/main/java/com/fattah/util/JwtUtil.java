package com.fattah.util;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private Long expiration;


    private SecretKey key;
    @PostConstruct
    public void init(){
      String pass= Encoders.BASE64.encode(secret.getBytes());
        this.key= Keys.hmacShaKeyFor(pass.getBytes());
    }

    public String generateToken(String username){
        Date date= new Date();
        Date expirationDate=new Date(date.getTime()+expiration);
      return   Jwts.builder()
                .signWith(key)
                .issuedAt(date)
                .subject(username)
                .expiration(expirationDate)
                .compact();
    }
    public String getUsername(String token){
        return Jwts.parser().verifyWith(key).build().
                parseSignedClaims(token).getPayload().getSubject();
    }
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("token is invalid");
            return false;
        }

    }
}
