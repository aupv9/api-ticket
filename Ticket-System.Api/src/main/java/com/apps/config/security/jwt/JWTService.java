package com.apps.config.security.jwt;

import com.apps.domain.entity.City;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.Payload;

import java.text.ParseException;

public interface JWTService {
    String generatorToken() throws JOSEException;
    boolean verifyToken(String token) throws ParseException, JOSEException;
    City getClaims(String token) throws ParseException;

}
