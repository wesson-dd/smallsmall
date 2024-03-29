package com.small.mapper;

import com.small.pojo.DynamicMapperParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Create By wesson 2020-09-20 20:42
 *
 * @author wesson
 */
@Mapper
@Repository
public interface DynamicMapper {
    /**
     * @param dynamicMapperParams
     * @return
     */
    int getTotalCountByParam(@Param("params") DynamicMapperParams dynamicMapperParams);

    /**
     * @param dynamicMapperParams
     * @return
     */
    List<LinkedHashMap<String, Object>> getResultListByParams(@Param("params") DynamicMapperParams dynamicMapperParams);

}
