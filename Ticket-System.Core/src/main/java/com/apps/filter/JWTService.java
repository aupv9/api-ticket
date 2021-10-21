package com.apps.filter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;

public interface JWTService {
    String generatorToken(String email) throws JOSEException;
    boolean verifyToken(String token) ;
    String getEmailFromToken(String token) ;

}
