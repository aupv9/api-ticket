package com.apps.filter;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;


@Component
@Slf4j
public class JWTServiceImp implements JWTService{

    RSAKey rsaJWK ;
    char[] keyId = new char[]{'1','6','3','6','9'};
    final int EXPIRE_TIME = 1500000;
    public JWTServiceImp() throws JOSEException {
        this.rsaJWK = new RSAKeyGenerator(2048)
                .keyID(Arrays.toString(keyId))
                .generate();
    }

    @Override
    public String generatorToken(String email) throws JOSEException {
        // RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures

        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(this.rsaJWK);


        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("author")
                .issuer("author")
                .claim("email",email)
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
    public boolean verifyToken(String token) {
        // On the consumer side, parse the JWS and verify its RSA signature
        RSAKey rsaPublicJWK = this.rsaJWK.toPublicJWK();

        boolean isVerified = false;
        try{
            JWSVerifier verifier =new RSASSAVerifier(rsaPublicJWK);
            isVerified = this.getSignedJWT(token).verify(verifier) && this.isTokenExpired(token) ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  isVerified;
    }

    @Override
    public String getEmailFromToken(String token) {
        JWTClaimsSet jwtClaimsSet = null;
        try{
            jwtClaimsSet =  this.getSignedJWT(token).getJWTClaimsSet();
            return jwtClaimsSet.getStringClaim("email");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private SignedJWT getSignedJWT(String token) throws ParseException {
        System.out.println(token);
        return SignedJWT.parse(token);
    }

    private Boolean isTokenExpired(String token) throws ParseException {
        Date expiration = this.getSignedJWT(token).getJWTClaimsSet().getExpirationTime();
        return expiration.after(new Date());
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

}
