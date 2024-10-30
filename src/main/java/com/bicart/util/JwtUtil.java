package com.bicart.util;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bicart.helper.JwtAuthenticationException;

@Component
public class JwtUtil {

    private static final String secretKey;
    private static final SecretKey key;
    private static final long EXPIRATION_TIME_MS = 60 * 60 * 500;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            key = Keys.hmacShaKeyFor(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new JwtException("Error in generating key", e);
        } catch (Exception e) {
            throw new JwtException("error in generating key", e);
        }
    }

    /**
     * <p>
     * Generates Token for every Login.
     * </p>
     *
     * @return the generated Token as a String
     * @Param username to generate token.
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .and()
                .signWith(key)
                .compact();
    }

    /**
     * <p>
     * Validates the Token with username and checks Expiration.
     * </p>
     *
     * @return the boolean value true if the token is valid else returns false.
     * @Param token and UserDetails.
     */
    public static boolean validateToken(String token, UserDetails userDetails)
            throws JwtAuthenticationException {
        try {
            final String username = extractUserName(token);
            return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new JwtAuthenticationException("Error in validating the token, invalid token");
        }
    }

    /**
     * <p>
     * Extracts the username from the token.
     * </p>
     *
     * @return the extracted username.
     * @Param token to extract the username.
     */
    public static String extractUserName(String token) throws JwtAuthenticationException {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * <p>
     * Extracts the claims from the token.
     * </p>
     *
     * @Param token to extract the claim.
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
            throws JwtAuthenticationException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * <p>
     * Checks the expiration of token.
     * </p>
     *
     * @return the boolean value true if the token is not expired else returns false.
     * @Param token to check expiration.
     */
    private static boolean isTokenExpired(String token) throws JwtAuthenticationException {
        return extractExpiration(token).before(new Date());
    }

    /**
     * <p>
     * Extracts the  date of Expiration from the token.
     * </p>
     *
     * @return date of Expiration.
     * @Param token to extract the date of Expiration.
     */
    private static Date extractExpiration(String token) throws JwtAuthenticationException {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Error in extracting expiration from token");
        }
    }

    /**
     * <p>
     * Extracts all the claims of the token
     * </p>
     *
     * @return the extracted claims.
     * @Param token to extract all claims.
     */
    public static Claims extractAllClaims(String token) throws JwtAuthenticationException {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid token or Token Expired");
        }
    }
}
