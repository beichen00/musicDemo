package com.woniuxy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;



@Data
@TableName("t_music")
@Builder
public class Music  {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Long createTime;
    private Integer visitCount;
    private Integer likesCount;
}
