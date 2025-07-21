package com.small.pojo;

import cn.hutool.core.annotation.Alias;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

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

    public static void main(String[] args) {
        ArrayList<TestEntity> objects = Lists.newArrayList();
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setADouble(1D);
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setADouble(2D);

        objects.add(testEntity1);
        objects.add(testEntity2);

        System.out.println(objects.stream().map(TestEntity::getADouble).max(Comparator.comparingDouble(Double::doubleValue)).orElse(3D));

    }
}
