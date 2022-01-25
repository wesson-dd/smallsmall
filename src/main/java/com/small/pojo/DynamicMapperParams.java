package com.small.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * Create By wesson 2020-09-20 20:37
 *
 * @author wesson
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DynamicMapperParams implements Serializable {
    private static final long serialVersionUID = -2029047708406518991L;

    private List<String> columns;
    private String tableName;
    private String updateStatement;
    private String sqlCondition;


}
