package com.oriseq.controller.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET_KEY = "lokghj44343g4grgdsg34gg4g4g4g4g4grge5ggdgege5ge5gdfgbnfnyfjfsgsrf3r4tfuvbh"; // 密钥
    private static final long EXPIRATION_TIME = 60 * 60 * 1000 * 24 * 3; //过期时间7天;

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            String subject = claimsJws.getBody().getSubject();
            System.out.println("subject: " + subject);
            Date expiration = claimsJws.getBody().getExpiration();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("expiration: " + simpleDateFormat.format(expiration));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
