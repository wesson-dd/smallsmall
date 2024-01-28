package com.small.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wesson
 * Create at 2022/3/24 10:48 周四
 */
@TableName("test")
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class TestPojo implements Serializable {
    @TableId( type = IdType.AUTO )
    private Integer id;
    private String name;
    private Date createTm;
    private Date updateTm;
}
