package com.small.mapstruct;

import cn.hutool.core.util.ObjectUtil;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author wesson
 * Create at 2023/6/26 00:55 周一
 */
@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "time", dateFormat = "dd-MM-yyyy", defaultValue = "06-10-2024")
    CarDto carToCarDto(Car car);

    @Condition
    default boolean isNotEmpty(Object value) {
        return ObjectUtil.isNotEmpty(value);
    }
}
