package com.learn.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zixiang Hu
 * @description 登录拦截器
 * @create 2020-10-07-19:03
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 目标方法前允许
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            // 这里可以使用重定向，不过那样就无法携带数据
            request.setAttribute("message","没有权限，请先登录");
            request.getRequestDispatcher("/admin").forward(request, response);

            // 如果没有登录就重定向到登录页面
//            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
