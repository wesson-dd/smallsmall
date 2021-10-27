package com.small.small.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 *
 * @author wesson
 * Create at 2021-04-04 16:30
 **/
@Configuration
public class MyConfig {
    @Value("${wesson.to}")
    private String to;
    @Value("${wesson.be}")
    private String be;
    @Value("${wesson.top}")
    private String top;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBe() {
        return be;
    }

    public void setBe(String be) {
        this.be = be;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }
}
