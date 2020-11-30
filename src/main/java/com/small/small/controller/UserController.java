package com.small.small.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * description:
 *
 * @author wesson
 * Create at 2020-12-01 03:10
 **/
@RestController
@RequestMapping(value = "/")
public class UserController {

    @GetMapping("/user")
    public String user (){
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        System.out.println("当前操作者username：" + principal);
        return "成功访问到user请求";
    }
    @GetMapping("/login")
    public String login (String username , String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);

        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        Subject subject = SecurityUtils.getSubject();
        Serializable tokenId = subject.getSession().getId();
        System.out.println(tokenId);
        return String.valueOf(tokenId);
    }
}
