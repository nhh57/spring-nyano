package com.sample.application.token;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;

/**
 * Secret key util
 *
 * @author hainh
 */
public class SecretKeyUtil {
    public static SecretKey generalKey() {
        // tuỳ chỉnh
        byte[] encodedKey = Base64.decodeBase64("cuAihCz53DZRjZwbsGcZJ2sajdkakqasAi6At+T142uphtJMsk7iQ=");
        SecretKey key = Keys.hmacShaKeyFor(encodedKey);
        return key;
    }

    public static SecretKey generalKeyByDecoders() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2sajdkakqasAi6At+T142uphtJMsk7iQ="));
    }
}