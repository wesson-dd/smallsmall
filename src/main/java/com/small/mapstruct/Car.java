package com.small.mapstruct;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wesson
 * Create at 2023/6/26 00:58 周一
 */
@Data
public class Car implements Serializable {
    private static final long serialVersionUID = -3918753105524326657L;

    private Integer id;
    private String str;
    private BigDecimal bigDecimal;
    private LocalDateTime time;
}
