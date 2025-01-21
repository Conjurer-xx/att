package com.att.acceptance.movie_theater.security;

import java.io.IOException;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String secret;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(String secret, UserDetailsService userDetailsService) {
        this.secret = secret;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                SignedJWT signedJWT = SignedJWT.parse(jwt);

                // Verify the JWT signature using the secret key
                JWSVerifier verifier = new MACVerifier(new SecretKeySpec(secret.getBytes(), "HmacSHA512"));
                if (!signedJWT.verify(verifier)) {
                    throw new RuntimeException("Invalid JWT signature");
                }

                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                String username = claims.getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication 
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); 
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication); 
            } catch (Exception e) {
                logger.error("Invalid JWT token: {}" + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}