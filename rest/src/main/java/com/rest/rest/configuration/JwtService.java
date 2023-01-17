package com.rest.rest.configuration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    private static final String SECRET_KEY = "6A586E3272357538782F413F4428472B4B6250645367566B5970337336763979";

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }
    public String generateToken(Map<String,Object> extraClaims , UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24 ))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isTokenValid(String token , UserDetails userDetails){
        String userName = extractUserName(token);
        System.out.println("is token valid  : "+userDetails.getUsername()+" \n user from jwt : "+userName);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token) ;
    }
    public boolean isTokenExpired(String token){
        if(extractExpiration(token).before(new Date(System.currentTimeMillis()))){
            return true;
        }else{
            return false;
        }
    }
    private Key getSingInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractUserName(String jwt){
        return extractClaim(jwt,Claims::getSubject);
    }
    public Date extractExpiration(String jwt){
        return extractClaim(jwt,Claims::getExpiration);
    }
    public <T> T extractClaim( String jwt , Function<Claims,T> claimResolver ){
        final Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }
    public Claims extractAllClaims(String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
