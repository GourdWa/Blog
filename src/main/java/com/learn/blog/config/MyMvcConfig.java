package com.learn.blog.config;

import com.learn.blog.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-17-15:08
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //加入我们自己定义的登录拦截器
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**/*").
                excludePathPatterns("/admin").
                excludePathPatterns("/admin/login");
    }

    /**
     * 添加视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器发送/admin/toIndex请求来到admin/index页面
        ViewControllerRegistration viewControllerRegistration = registry.addViewController("/admin/toIndex");
        viewControllerRegistration.setViewName("admin/index");
    }
}
