package com.small.mapstruct;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wesson
 * Create at 2023/6/26 00:58 周一
 */
@Data
public class CarDto implements Serializable {
    private static final long serialVersionUID = -3918753105524326657L;

    private Long id;
    private String str = "defaultValue";
    private String bigDecimal;
    private String time;
}
