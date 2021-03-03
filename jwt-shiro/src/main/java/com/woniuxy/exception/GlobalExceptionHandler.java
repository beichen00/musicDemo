package com.woniuxy.exception;

import com.woniuxy.dto.Result;
import com.woniuxy.dto.Status;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//返回json格式
public class GlobalExceptionHandler{
    @ExceptionHandler({NullPointerException.class})
    public Result handlerNullPointException(){
        return new Result(false, Status.ERROR,"空指针异常",null);
    }
    @ExceptionHandler({Exception.class})
    public Result handlerException(){
        return new Result(false, Status.ERROR,"服务器异常",null);
    }
    @ExceptionHandler({AuthorizationException.class})
    public Result handlerAuthorizationException(){
        return new Result(false,Status.ACCESSERROR,"权限不足");
    }
}