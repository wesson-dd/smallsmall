package com.small.small.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * description:
 *
 * @author wesson
 * Create at 2020-12-01 02:56
 **/

public class MyRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        System.out.println(token);
        if (!"name".equals(token.getPrincipal())) {
            throw new IncorrectCredentialsException("用户名或密码不正确");
        }
        return new SimpleAuthenticationInfo("name", "", "MyRealm");
    }
}
