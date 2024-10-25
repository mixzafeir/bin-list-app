package com.interview.etravli.utils;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.interview.etravli.enums.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    private final String SECRET_KEY = "${jwt.secret}";

    public String generateToken(String username, String role) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .sign(algorithm);
    }

    public boolean validateToken(String token){
        return decodedJwt(token) != null;
    }

    public String getUsernameFromToken(String token){
        return decodedJwt(token).getSubject();
    }

    public String getRoleFromToken(String token){
        return decodedJwt(token).getClaim("role").asString();
    }


    public DecodedJWT decodedJwt(String token) {
        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
            throw new JWTVerificationException(ExceptionMessages.INVALID_JWT_SIGNATURE.toString());
        }
        return decodedJWT;
    }

}
