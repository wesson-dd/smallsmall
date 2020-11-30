package com.small.small.shiro;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description:
 *
 * @author wesson
 * Create at 2020-12-01 02:59
 **/
@Configuration
public class ShiroConfig {
    /**
     * 所有Subject 实例都必须绑定到一个SecurityManager上,SecurityManager 是 Shiro的核心，初始化时协调各个模块运行。然而，一旦 SecurityManager协调完毕，
     * SecurityManager 会被单独留下，且我们只需要去操作Subject即可，无需操作SecurityManager 。 但是我们得知道，当我们正与一个 Subject 进行交互时，实质上是
     * SecurityManager在处理 Subject 安全操作
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setSuccessUrl("http://www.baidu.com");

        Map<String, Filter> customFilterMap = new LinkedHashMap<>();
        customFilterMap.put("corsAuthenticationFilter", new CorsAuthenticationFilter());
        shiroFilter.setFilters(customFilterMap);

        //拦截器，配置访问权限 必须是LinkedHashMap，因为它必须保证有序。滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        Map<String, String> filterMap = new LinkedHashMap<String, String>();

        filterMap.put("/login", "anon");
        //剩余的请求shiro都拦截
        filterMap.put("/**/*", "authc");

        shiroFilter.setFilterChainDefinitionMap(filterMap);
        // 配置复杂请求过滤器

        return shiroFilter;
    }
    /**
     * securityManager 核心配置
     * 安全控制层
     */
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设置自定义的realm
        defaultWebSecurityManager.setRealm(myRealm());
        //自定义的shiro session 缓存管理器
        defaultWebSecurityManager.setSessionManager(sessionManager());

        return defaultWebSecurityManager;
    }


    /**
     * 自定义的realm
     */
    @Bean
    public MyRealm myRealm() {
        return new MyRealm();
    }


    /**
     * 开启shiro 的AOP注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 自定义的 shiro session 缓存管理器，用于跨域等情况下使用 token 进行验证，不依赖于sessionId
     */
    @Bean
    public SessionManager sessionManager(){
        //将我们继承后重写的shiro session 注册
        ShiroSession shiroSession = new ShiroSession();
        //如果后续考虑多tomcat部署应用，可以使用shiro-redis开源插件来做session 的控制，或者nginx 的负载均衡

        shiroSession.setSessionDAO(new EnterpriseCacheSessionDAO());
        return shiroSession;
    }
    @Bean
    public Authenticator authenticator(){
      return new ModularRealmAuthenticator();
    }
    @Bean
    public Authorizer authorizer(){
        return new ModularRealmAuthorizer();
    }
}
