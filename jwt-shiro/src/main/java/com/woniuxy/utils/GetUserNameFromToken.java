package com.woniuxy.utils;

import javax.servlet.http.HttpServletRequest;

public class GetUserNameFromToken {
    public static String getUsername(HttpServletRequest request){
        String token = request.getHeader("Token");
        String username = JWTUtils.decodedJWT(token).getClaim("username").asString();
        return username;
    }
}
