package com.small.small.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * <p>
 * Create By wesson 2020-09-20 20:37
 *
 * @author wesson
 */
@NoArgsConstructor
@Data
public class DynamicMapperParams implements Serializable {
    private static final long serialVersionUID = -2029047708406518991L;

    private List<String> columns;
    private String tableName;
    private String updateStatement;
    private String sqlCondition;


}
