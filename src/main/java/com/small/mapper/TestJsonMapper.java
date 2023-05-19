package com.small.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.small.pojo.TestJson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wesson
 * @description 针对表【test_json】的数据库操作Mapper
 * @createDate 2023-05-19 19:34:29
 * @Entity com.small.pojo.TestJson
 */
@Mapper
@Repository
public interface TestJsonMapper extends BaseMapper<TestJson> {

    List<TestJson> selectListXml();

    List<TestJson> selectListXml2();

    void set(@Param("value") String value, @Param("id") Integer id);

    void remove(@Param("value") String value, @Param("id") Integer id);
}




