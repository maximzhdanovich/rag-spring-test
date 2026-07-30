package com.example.MyBookShopApp.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil {

  private final JWTBlacklistRepository jwtBlacklistRepository;

  public JWTUtil(
      JWTBlacklistRepository jwtBlacklistRepository) {
    this.jwtBlacklistRepository = jwtBlacklistRepository;
  }

  @Value("${auth.secret}")
  private String secret;



  private String createToken(Map<String, Object> claims, String username){
    return Jwts
        .builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(SignatureAlgorithm.HS256, secret).compact();
  }

  public String generateToken(UserDetails userDetails){
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername());
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {

    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

  }

  public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }

  public Boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  public Boolean isTokenBlacklisted(String token){
    JWTBlackListEntity entity = jwtBlacklistRepository.findJWTBlackListEntityByToken(token);
    if (entity != null) {
      Logger.getLogger(this.getClass().getSimpleName())
          .warning("Blacklisted token founded " + entity.getToken());
    }
    return jwtBlacklistRepository.findJWTBlackListEntityByToken(token) != null;
  }

  public Boolean validateToken(String token, UserDetails userDetails) {

      String username = extractUsername(token);
      return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenBlacklisted(token));

  }




}
