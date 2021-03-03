package com.woniuxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: 北陈
 * @Date: 2021/1/31 00:43
 * @Description:
 */
@Configuration
public class SetCrossOrigin implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置哪些路径可以跨越
        registry.addMapping("/**")
                .allowedOrigins("*")//设置哪些授权源发来的请求可以跨域
                .allowedMethods("*")//设置跨越时发送请求的方式
                .allowedHeaders("*");//设置哪些请求头可以发送跨域请求
    }
}
