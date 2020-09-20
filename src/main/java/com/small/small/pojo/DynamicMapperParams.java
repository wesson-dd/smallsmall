package com.small.small.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * <p>
 * Create By wesson 2020-09-20 20:37
 * @author wesson
 */

public class DynamicMapperParams implements Serializable {

    private static final long serialVersionUID = -2029047708406518991L;

    private List<String> columns;
    private String tableName;
    private String updateStatement;
    private String sqlCondition;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUpdateStatement() {
        return updateStatement;
    }

    public void setUpdateStatement(String updateStatement) {
        this.updateStatement = updateStatement;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public void setSqlCondition(String sqlCondition) {
        this.sqlCondition = sqlCondition;
    }
}
