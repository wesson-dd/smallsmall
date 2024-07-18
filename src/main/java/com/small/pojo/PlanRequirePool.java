package com.small.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线路
 *
 * @author wesson
 * Created on 2024/6/30 下午11:14
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlanRequirePool {
    private Long id;
    private String kao;
    private String fa;
    private String dao;
    private String jie;
}
