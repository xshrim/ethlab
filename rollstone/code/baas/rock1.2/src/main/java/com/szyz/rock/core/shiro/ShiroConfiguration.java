package com.szyz.rock.core.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("===================shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/sso/login.action", "anon");
        filterChainDefinitionMap.put("/user/register.action", "anon");
        filterChainDefinitionMap.put("/user/checkEmail.action", "anon");
        filterChainDefinitionMap.put("/test/test.action", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //认证访问
        //filterChainDefinitionMap.put("/**", "authc");
        // 未登录时要跳转
        //shiroFilterFactoryBean.setLoginUrl("/noLogin.action");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/menu/index");
        //未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/error");

        Map<String,Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("loginFilter",new MyFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        filterChainDefinitionMap.put("/**", "loginFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyShiroRealm myShiroRealm(){
        System.out.println("===================myShiroRealm");
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        System.out.println("===================hashedCredentialsMatcher");
        HashedCredentialsMatcher hashedCredentialsMatcher =
                new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); // md5加密
        hashedCredentialsMatcher.setHashIterations(1); //md5一次
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    @Bean
    public SecurityManager securityManager(){
        System.out.println("===================securityManager" );
        DefaultWebSecurityManager securityManager =
                new DefaultWebSecurityManager();
        //设置缓存
        securityManager.setCacheManager(ehCacheManager());

        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    //缓存管理
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager c = new EhCacheManager();
        c.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return c;
    }


}
