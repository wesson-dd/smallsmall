package com.small.pojo;

import cn.hutool.core.annotation.Alias;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wesson
 * Create at 2022/3/24 10:48 周四
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
public class TestEntity implements Serializable {

    private static final long serialVersionUID = -4725420540624500836L;
    @Alias("我")
    private String name = "name";
    @Alias("要")
    private Integer type = 1;
    @Alias("飞")
    private Integer id = 1;
    @Alias("上")
    private Boolean aBoolean = true;
    @Alias("天")
    private Long aLong = 2L;

    private Double aDouble = 2D;

    private BigDecimal bigDecimal = new BigDecimal("0");
    private LocalDateTime time;
}
