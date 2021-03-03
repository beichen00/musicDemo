package com.woniuxy.dto;

/**
 * @Auther: 北陈
 * @Date: 2021/1/26 15:35
 * @Description:
 */
public class Status {

    public static final int OK = 20000;//成功
    public static final int ERROR = 20001;//失败
    public static final int LOGINERROR = 20002;//用户名或密码错误
    public static final int ACCESSERROR = 20003;//权限不足
    public static final int NULLPOINTERROR = 20004;//空指针异常
    public static final int ARRAYINDEXOUTOFBOUNDSERROR = 20005;//数组溢出异常
    public static final int UNKNOWNTYPEERROR = 20006;//未知类型异常
    public static final int ARITHMETICERROR = 20007;//数字算数异常
    public static final int USEREXIST=20008;//用户名已存在
    public static final int CREDENTAILERROR =2009;//密码错误
    public static final int PRINCINPALERROR =2010;//用户名不存在
}
