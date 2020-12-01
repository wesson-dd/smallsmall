package com.small.small.TkMybatis通用类;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 使用接口中的方法即可  操作单表不用新建xml sql
 * @author wesson
 * @create 2020/9/7
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
//FIXME 特别注意，该接口不能被扫描到，否则会出错
}
