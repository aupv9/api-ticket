package com.apps.config.security.jwt;

import com.apps.domain.entity.City;
import com.apps.service.CityService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JWTServiceImp implements JWTService{
    @Autowired
    private CityService cityService;

    RSAKey rsaJWK ;
    char[] keyId = new char[]{'1','6','3','6','9'};
    final int EXPIRE_TIME = 15000;
    public JWTServiceImp() throws JOSEException {
        this.rsaJWK = new RSAKeyGenerator(2048)
                .keyID(Arrays.toString(keyId))
                .generate();
    }

    @Override
    public String generatorToken() throws JOSEException {
        // RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(this.rsaJWK);


        Map<String, City> map = new HashMap<>();
        map.put("city",cityService.findByState("CA"));
        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(this.generateExpirationDate())
                .claim("payload", map)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                claimsSet);

        // Compute the RSA signature
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Override
    public boolean verifyToken(String token) throws ParseException, JOSEException {
        // On the consumer side, parse the JWS and verify its RSA signature
        RSAKey rsaPublicJWK = this.rsaJWK.toPublicJWK();
        JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
        Map<String,City> map = (Map<String, City>) this.getSignedJWT(token).getJWTClaimsSet().getClaim("payload");
        log.info("Token Exprired: " + this.isTokenExpired(token));
        return this.getSignedJWT(token).verify(verifier);
    }

    @Override
    public City getClaims(String token) throws ParseException {
        return null;
    }

    private SignedJWT getSignedJWT(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private Boolean isTokenExpired(String token) throws ParseException {
        Date expiration = this.getSignedJWT(token).getJWTClaimsSet().getExpirationTime();
        log.info("expiration: "+ expiration);
        log.info("now: "+ new Date());
        return expiration.before(new Date());
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

}
