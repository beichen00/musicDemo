package com.woniuxy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

public class JWTUtils {

    private static final String SIGN="abcdefg1234";
    private static final Long TIMEOUT=6*60*60*1000L;
    //创建token
    public static String getToken(HashMap<String,String> hashMap){
        JWTCreator.Builder builder = JWT.create();
        hashMap.forEach((k,v)->{
            builder.withClaim(k,v);
        });
//        if (sign==null){
//            sign=SaltUtils.getSalt(5);
//        }
        String jwtToken = builder.withExpiresAt(new Date(System.currentTimeMillis()+TIMEOUT))
                .sign(Algorithm.HMAC256(SIGN));
        return jwtToken;
    }

    //解析token，获得DecodedJWT
    public static DecodedJWT decodedJWT(String token){
       return JWT.decode(token);
    }

    public static boolean comparaToAuthentication(String token,String username){
        return decodedJWT(token).getClaim("username").asString().equals(username);
    }

}
