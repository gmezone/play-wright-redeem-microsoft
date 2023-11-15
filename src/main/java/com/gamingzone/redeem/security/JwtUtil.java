package com.gamingzone.redeem.security;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
@Component
public class JwtUtil {
    private static final String KEY_STRING = "GtdPvDfyfegbARYG";

    private static final String EXP = "exp";
    static AesKey key  = new AesKey(KEY_STRING.getBytes(StandardCharsets.ISO_8859_1));
    static private Signer signer;
    static SignatureVerifier verifier;
    static private JsonParser objectMapper = JsonParserFactory.create();
    static {
        setKeyPair();
    }
    static public void setKeyPair(){
        System.out.println("setKeyPair");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"),
                "GameZoneQ!W@".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt");
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA");
        signer = new RsaSigner((RSAPrivateKey) privateKey);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        verifier = new RsaVerifier(publicKey);
    }
    static public String encode(String content){
        System.out.println("content =" + content);
        JSONObject jsonObject = new JSONObject(content);
        Date extDate = new Date();
        long exTime  =(extDate.getTime() /1000L) + 360;
        jsonObject.remove(EXP);
        jsonObject.put(EXP,exTime);
        content = jsonObject.toString();
        String serializedJwe;
        try{
            JsonWebEncryption jwe  = new JsonWebEncryption();
            jwe.setPayload(content);
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
            jwe.setKey(key);
            jwe.enableDefaultCompression();
            serializedJwe = jwe.getCompactSerialization();
        } catch (Exception e){
            throw  new IllegalStateException("Cannot convert access token to JSON" ,e);
        }
        String token  = JwtHelper.encode(serializedJwe ,signer).getEncoded();
        System.out.println("token=" + token);
        return token;
    }

    public Map<String ,Object> getValuesFromToken(String token){
        return decode(token);

    }

    public boolean validateToken(Map<String , Object> jwtValues ){
        return  (!isTokenExpired(jwtValues));
    }

    private boolean isTokenExpired(Map<String, Object> jwtValues) {
        Date exDate;
        if (jwtValues.get(EXP) instanceof  String){
            exDate = new Date(Long.parseLong((String) jwtValues.get(EXP) ) * 1000L);
        } else {
            Integer d =(Integer) jwtValues.get(EXP);
            exDate = new Date(d * 1000L);
        }
        return  exDate.before(new Date());
    }

    public static Map<String , Object> decode(String token){
        Map<String ,Object> map = null;
        try{
            Jwt jwt = JwtHelper.decodeAndVerify(token,verifier);
            String content = jwt.getClaims();
            if(!content.startsWith("{")){
                System.out.println("content:" + content);
                JsonWebEncryption jwe = new JsonWebEncryption();
                jwe.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST,
                        KeyManagementAlgorithmIdentifiers.A128KW));
                jwe.setContentEncryptionAlgorithmConstraints(
                        new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST,
                                ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
                jwe.setKey(key);
                System.out.println("key: " + key);
                jwe.setCompactSerialization(content);
                content = jwe.getPayload();
            }
            map = objectMapper.parseMap(content);
        } catch (Exception e){
            e.printStackTrace();
            throw  new InvalidTokenException("Cannot convert access token to JSON");
        }
        if (map == null){
            throw  new InvalidTokenException("Token was not recognised");
        }
        if(map.containsKey(EXP)){
            System.out.println("exp:" + map.get(EXP));
            Date exDate;
            if (map.get(EXP) instanceof String) {
                exDate = new Date(Long.parseLong((String) map.get(EXP)) * 1000L);
               } else {
                Integer d = (Integer) map.get(EXP);
                exDate = new Date(d * 1000L);
              }
             if (exDate.before(new Date())){
                 System.out.println("exDate: " + exDate);
                 throw new InvalidTokenException("Token has expired");
            }
        }
        return map;

    }
}
