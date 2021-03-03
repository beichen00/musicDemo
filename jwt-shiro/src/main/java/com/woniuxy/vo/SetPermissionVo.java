package com.woniuxy.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: 北陈
 * @Date: 2021/2/20 00:56
 * @Description:
 */
@Data
public class SetPermissionVo {
    private Integer rid;
    private List<Integer> pids;
}
