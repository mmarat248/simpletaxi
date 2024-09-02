package com.example.demo.configurations.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.models.auth.AuthPayload;
import com.example.demo.models.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class JWTTokenProvider {

    @Value("${application.security.jwt.token.secret-key}")
    private String JWT_SECRET;

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim(JWTClaimNames.USERNAME.getValue(), user.getUsername())
                .withExpiresAt(genAccessExpirationDate())
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public AuthPayload validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
            return new AuthPayload(
                UUID.fromString(decodedJWT.getSubject()),
                decodedJWT.getClaim(JWTClaimNames.USERNAME.getValue()).asString()
            );
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    private Instant genAccessExpirationDate() {
        // TODO: move to config
        int DURATION_IN_HOURS = 24;
        return LocalDateTime.now().plusHours(DURATION_IN_HOURS).atZone(ZoneId.systemDefault()).toInstant();
    }

    @Getter
    private enum JWTClaimNames {
        USERNAME("un");
        private final String value;

        JWTClaimNames(String value) {
            this.value = value;
        }

    }
}
