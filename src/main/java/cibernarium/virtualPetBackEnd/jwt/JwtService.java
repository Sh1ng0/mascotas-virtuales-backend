package cibernarium.virtualPetBackEnd.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.function.Function;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.GrantedAuthority;

// LOGGER MODE

@Service
public class JwtService {

    private static final String SECRET_KEY = "4Bd23Gk1M8wD2R0zI7eV9C1qF8yT3JvD5U2Kz0LhZ7PpK1Q";
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public String getToken(UserDetails user) {
        logger.debug("Generando token para el usuario: " + user.getUsername());
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {

        extraClaims.put("authorities", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug("Token generado: " + token);
        return token;
    }

    private Key getKey() {
        logger.debug("Decodificando clave secreta para firmar el token.");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        logger.debug("Extrayendo username del token: " + token);
        String username = getClaim(token, Claims::getSubject);
        logger.debug("Username extraído: " + username);
        return username;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.debug("Validando token: " + token + " para el usuario: " + userDetails.getUsername());
        final String username = getUsernameFromToken(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        logger.debug("Token válido: " + isValid);
        return isValid;
    }

    private Claims getAllClaims(String token) {
        logger.debug("Obteniendo todos los claims del token.");
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        logger.debug("Claims obtenidos: " + claims);
        return claims;
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        logger.debug("Resolviendo claim del token.");
        final Claims claims = getAllClaims(token);
        T claim = claimsResolver.apply(claims);
        logger.debug("Claim resuelto: " + claim);
        return claim;
    }

    private Date getExpiration(String token) {
        logger.debug("Obteniendo fecha de expiración del token.");
        Date expiration = getClaim(token, Claims::getExpiration);
        logger.debug("Fecha de expiración: " + expiration);
        return expiration;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = getExpiration(token).before(new Date());
        logger.debug("¿Token expirado?: " + expired);
        return expired;
    }
}

//@Service
//public class JwtService {
//
//    private static final String SECRET_KEY = "4Bd23Gk1M8wD2R0zI7eV9C1qF8yT3JvD5U2Kz0LhZ7PpK1Q";
//        // private static final String SECRET_KEY_STRING = new String(SECRET_KEY.getEncoded());
//
//
//    public String getToken(UserDetails user) {
//        return getToken(new HashMap<>(), user);
//    }
//
//    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(user.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
//                .signWith(getKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key getKey() {
//        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String getUsernameFromToken(String token) {
//        return getClaim(token, Claims::getSubject);
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username=getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
//    }
//
//    private Claims getAllClaims(String token)
//    {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
//    {
//        final Claims claims=getAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Date getExpiration(String token)
//    {
//        return getClaim(token, Claims::getExpiration);
//    }
//
//    private boolean isTokenExpired(String token)
//    {
//        return getExpiration(token).before(new Date());
//    }
//
//
//
//}
//


