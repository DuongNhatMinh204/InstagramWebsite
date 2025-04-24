package com.nminh.websiteinstagram.security;

import com.nminh.websiteinstagram.enums.ErrorCode;
import com.nminh.websiteinstagram.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey ;

    // lấy subject(username người dùng)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // lấy 1 claim bất kì
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // lấy tất cả claim
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(secretKey).
                build().
                parseClaimsJws(token).
                getBody();

    }

    // kiểm tra tính hợp lệ của token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if(isTokenExpired(token)) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        return username.equals(userDetails.getUsername()) ;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
