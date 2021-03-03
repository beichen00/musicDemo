package com.woniuxy.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: 北陈
 * @Date: 2021/2/3 23:58
 * @Description:
 */
@Data
public class UserRoles {

    private List<String> checkList;
    private Integer uid;
}
