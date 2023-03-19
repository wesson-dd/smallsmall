package com.small.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.small.pojo.TestPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Create By wesson 2020-09-20 20:42
 *
 * @author wesson
 */
@Mapper
@Repository
public interface TestMapper extends BaseMapper<TestPojo> {

    List<TestPojo> getTestInfo();

    List<TestPojo> findLike(@Param("code") String code);


    List<TestPojo> findLike2(@Param("code") String code);
}
