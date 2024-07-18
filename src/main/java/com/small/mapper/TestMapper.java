package com.small.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.small.pojo.NameDto;
import com.small.pojo.TestPojo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Create By wesson 2020-09-20 20:42
 *
 * @author wesson
 */
@Mapper
@Repository
public interface TestMapper extends BaseMapper<TestPojo> {

    List<TestPojo> getTestInfo();

    @MapKey("id_na")
    Map<String, NameDto> getMap();

    List<TestPojo> findLike(@Param("code") String code);


    List<TestPojo> findLike2(@Param("code") String code);

    List<TestPojo> getByForeach(@Param("list") List<String> list, @Param("list2") List<String> list2);
}
