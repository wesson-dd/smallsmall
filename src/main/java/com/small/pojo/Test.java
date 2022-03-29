package com.small.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wesson
 * Create at 2022/3/24 10:48 周四
 */
@Data
public class Test implements Serializable {

    private static final long serialVersionUID = -4725420540624500836L;
    private String name;
    private Integer type;
    private Integer id;
}
