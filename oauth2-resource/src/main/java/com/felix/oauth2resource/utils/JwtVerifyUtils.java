package com.felix.oauth2resource.utils;

import io.jsonwebtoken.Jwts;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JwtVerifyUtils {

    public static void verifyRSA(String publicKey, String accessToken) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyByteServer = Base64.getDecoder().decode(publicKey);
        PublicKey publicKeyServer = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
        Jwts.parserBuilder().setSigningKey(publicKeyServer).build().parseClaimsJws(accessToken).getBody();
    }
    
}
