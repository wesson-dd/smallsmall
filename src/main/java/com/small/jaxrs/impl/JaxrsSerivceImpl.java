package com.small.jaxrs.impl;

import com.small.jaxrs.JaxrsInterface;
import org.springframework.stereotype.Service;

/**
 * @author wesson
 * Create at 2023/4/3 23:15 周一
 */
@Service
public class JaxrsSerivceImpl implements JaxrsInterface {
    /**
     * @return
     */
    @Override
    public String getInfo() {

        return "加油哦，wesson";
    }
}
