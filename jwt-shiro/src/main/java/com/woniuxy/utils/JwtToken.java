package com.woniuxy.utils;

import org.apache.shiro.authc.AuthenticationToken;

//自定义token方法，AuthenticationToken,获取凭证和身份信息，返回值传入token
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token){
        this.token=token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
