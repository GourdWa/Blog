package com.learn.blog.aspect;


import com.learn.blog.utils.IpAddressUtil;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Zixiang Hu
 * @description 定义一个AOP切面类
 * @create 2020-09-13-20:36
 */
@Component
@Aspect
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    // controller里面所有类的所有方法都切入
    // ..表示匹配任意多个参数或者任意多层路径；*表示匹配一个或多个字符，匹配任意一层路径，匹配任意一个参数
    @Pointcut("execution(* com.learn.blog.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取请求的URL
        String url = request.getRequestURL().toString();
        // 获取请求的ip
//        String ip = request.getRemoteAddr();
        String ip = IpAddressUtil.getIpAddress(request);
        // 获得调用的方法签名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 获得传递进方法的参数
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
//        logger.info("-----doAfter-----");
    }

    @AfterReturning(pointcut = "log()", returning = "result")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }

    /**
     * 定义日志内部类
     */
    @Data
    public class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog() {
        }

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }
    }
}
