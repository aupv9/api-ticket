package com.apps.config.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;


@Service
@Slf4j
public class JWTServiceImp implements JWTService{


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


        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .expirationTime(this.generateExpirationDate())
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
        log.info("Token Exprired: " + this.isTokenExpired(token));
        return this.getSignedJWT(token).verify(verifier) && this.isTokenExpired(token);
    }

    @Override
    public Object getClaims(String token) throws ParseException {
        return null;
    }

    private SignedJWT getSignedJWT(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private Boolean isTokenExpired(String token) throws ParseException {
        Date expiration = this.getSignedJWT(token).getJWTClaimsSet().getExpirationTime();
        log.info("expiration: "+ expiration);
        log.info("now: "+ new Date());
        return expiration.after(new Date());
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

}
