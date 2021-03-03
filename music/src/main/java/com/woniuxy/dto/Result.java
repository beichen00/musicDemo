package com.woniuxy.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: 北陈
 * @Date: 2021/1/26 15:33
 * @Description:
 */

@Data
public class Result<T> implements Serializable {
    private boolean flag;//是否成功
    private Integer code;//响应状态码
    private String message;//响应信息
    private T data;//传回的数据

    public Result(boolean flag, Integer code, String message, Object data){
        this.flag=flag;
        this.code=code;
        this.message=message;
        this.data=(T)data;
    }

    public Result(boolean flag, Integer code, String message){
        this.flag=flag;
        this.code=code;
        this.message=message;
    }

    public Result(){
        this.flag=true;
        this.code=2000;
        this.message="操作成功";
    }
}

