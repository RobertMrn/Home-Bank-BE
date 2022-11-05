package org.Service.Services.JwtToken;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenGeneration implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;
    public String getEmailFromToken(String token) {
        return getAllDataFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getAllDataFromToken(token, Claims::getExpiration);
    }
    public String generateToken(Map<String, Object> data,UserDetails userDetails) {
        return Jwts.builder().setClaims(data).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.of("Europe/Bucharest")).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    private <T> T getAllDataFromToken(String token, Function<Claims, T> claimsResolver ) {
        final Claims dataFromToken =  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(dataFromToken);
    }
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
