package com.rhokart.usermgmt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtHelper {

    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final Algorithm accessTokenAlgo;
    private final Algorithm refreshTokenAlgo;
    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;

    public JwtHelper(@Value("${accessTokenSecret}") String accessTokenSecret,
                     @Value("${refreshTokenSecret}") String refreshTokenSecret,
                     @Value("${refreshTokenExpirationMinutes}") int refreshTokenExpirationMinutes,
                     @Value("${accessTokenExpirationMinutes}") int accessTokenExpirationMinutes)
    {
        log.info("Access Token [{}]", accessTokenSecret);
        accessTokenExpiration = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpiration = (long) refreshTokenExpirationMinutes * 60 * 1000;
        accessTokenAlgo = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgo = Algorithm.HMAC512(refreshTokenSecret);

        accessTokenVerifier = JWT.require(accessTokenAlgo).build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgo).build();

    }

    public String generateAccessToken(String userId, Date loginTime) {
        return JWT.create()
                .withSubject(userId)
                .withIssuedAt(loginTime)
                .withExpiresAt(new Date(new Date().getTime() + accessTokenExpiration))
                .sign(accessTokenAlgo);
    }

    public String generateRefreshToken(String userId, String tokenId) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("tokenId", tokenId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + refreshTokenExpiration))
                .sign(refreshTokenAlgo);
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        log.info("decoding refresh token here... token :{}",token);
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            refreshTokenAlgo.verify(decodedJWT);
            log.info("jwt:{}",decodedJWT.getHeader());

            return Optional.of(refreshTokenVerifier.verify(token));

        } catch (JWTVerificationException ex) {
            log.error("Invalid refresh token. Exception ::{}", ex.getMessage());
        }
        return Optional.empty();
    }

    public boolean validateRefreshToken(String token) {
        return decodeRefreshToken(token).isPresent();
    }

    public String getTokenIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getClaim("tokenId").asString();
    }
}
