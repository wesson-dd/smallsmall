package com.small.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
public class DynamicMapperParams implements Serializable {
    private static final long serialVersionUID = -2029047708406518991L;

    private String tableName;
    private List<String> columns;
    private String sqlCondition;


}
