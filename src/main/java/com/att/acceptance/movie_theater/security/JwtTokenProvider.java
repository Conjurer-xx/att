package com.att.acceptance.movie_theater.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs:86400000}")
    private int jwtExpirationMs; // Default to 1 day if not specified

    /**
     * Generate a JWT token for an authenticated user.
     *
     * @param authentication The authentication object.
     * @return The generated JWT token.
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .claim("username", userDetails.getUsername())
                    .issueTime(new Date(System.currentTimeMillis()))
                    .expirationTime(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(new MACSigner(jwtSecret.getBytes()));

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Extract the username (email) from the token.
     *
     * @param token The JWT token.
     * @return The username.
     */
    public String getUsernameFromToken(String token) {
        return extractClaim(token, claims -> (String) claims.getClaim("username"));
    }

    /**
     * Validate the JWT token.
     *
     * @param token The JWT token.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new MACVerifier(jwtSecret.getBytes()));
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    /**
     * Extract claims using a resolver function.
     *
     * @param token The JWT token.
     * @param claimsResolver A function to resolve claims.
     * @param <T> The type of claim to extract.
     * @return The resolved claim.
     */
    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        final JWTClaimsSet claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private JWTClaimsSet extractAllClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing JWT claims", e);
        }
    }
}