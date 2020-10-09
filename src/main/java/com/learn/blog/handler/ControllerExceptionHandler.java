package com.learn.blog.handler;

import com.learn.blog.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-09-13-19:43
 */
//用于标识全局性的控制器的拦截器，其basePackages或annotations属性将指定作用域那些控制器，默认是作用域所有的控制器
@ControllerAdvice
public class ControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 自定义异常拦截方法并返回指定的视图
     * @ExceptionHandler 注解能拦截指定的异常。当发生异常时，能够用指定的视图响应
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception ex) throws Exception {
      /*  if (ex instanceof NotFoundException)
            throw ex;*/
        // 抛出我们自定义的异常
        // AnnotationUtils.findAnnotation查找第一个参数是否包含ResponseStatus类注解
        // 如果包含了状态码的枚举，将异常抛出就不需要拦截该异常
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
            throw ex;
        }
        //记录异常信息
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(), ex.getMessage());
        // 定义一个ModelAndView将需要传递到模板中的数据存放在之中
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("exception", ex);
        //设置返回的页面
        modelAndView.setViewName("/error/error");
        return modelAndView;
    }
}
