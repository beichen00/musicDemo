package com.woniuxy.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;

public class Test {
    public static void main(String[] args) {
        String token = JWT.create().withClaim("name", "tom")
                .withClaim("age", 25)
                .sign(Algorithm.HMAC256("abcdjsjdlg"));
        System.out.println(token);
        Verification verification = JWT.require(Algorithm.HMAC256("abcdjsjdlg"));
        System.out.println(verification.build().verify(token).getClaim("name").asString());
    }
}
