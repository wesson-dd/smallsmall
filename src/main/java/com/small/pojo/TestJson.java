package com.small.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @TableName test_json
 */
@TableName(value = "test_json", autoResultMap = true)
@Data
@EqualsAndHashCode
public class TestJson implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;


    private String name;

    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private JsonEntry myJson;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
