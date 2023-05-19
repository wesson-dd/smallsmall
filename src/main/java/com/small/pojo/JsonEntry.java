package com.small.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wesson
 * Create at 2023/5/19 19:41 周五
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class JsonEntry implements Serializable {

    private static final long serialVersionUID = -1837007768766979005L;

    private String jan;
    private String feb;
    private String mar;
    private String apr;
    private String may;
}
