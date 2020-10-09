package com.learn.blog.config;

import com.learn.blog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Zixiang Hu
 * @description 拦截器配置类别
 * @create 2020-10-07-19:06
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //加入我们自己定义的登录拦截器
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**/*").
                excludePathPatterns("/admin").
                excludePathPatterns("/admin/login");
    }
}
