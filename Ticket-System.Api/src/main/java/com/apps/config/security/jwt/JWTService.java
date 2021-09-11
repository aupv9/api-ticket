package com.apps.config.security.jwt;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JWTService {
    String generatorToken() throws JOSEException;
    boolean verifyToken(String token) throws ParseException, JOSEException;
    Object getClaims(String token) throws ParseException;

}
