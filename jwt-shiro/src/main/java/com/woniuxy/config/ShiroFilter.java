package com.woniuxy.config;

import com.woniuxy.Custom.MyRealm;

import com.woniuxy.filter.JwtFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import org.apache.shiro.realm.Realm;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: 北陈
 * @Date: 2021/1/22 17:02
 * @Description:
 */
@Configuration
public class ShiroFilter {

    @Bean
    public Realm realm(){

        MyRealm myRealm=new MyRealm();

        return myRealm;
    }



//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
//        return authorizationAttributeSourceAdvisor;
//    }




        @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());

        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("jwt",new JwtFilter());
        linkedHashMap.put("/user/login","anon");
        linkedHashMap.put("/user/register","anon");
        linkedHashMap.put("/**","jwt");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(linkedHashMap);
        return shiroFilterFactoryBean;


    }



}
