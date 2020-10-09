package com.learn.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-09-13-20:11
 */
// 自定义异常，并标注返回状态码。也就是说当抛出这个异常的时候页面会显示指定的状态码
// 如果定义了对应的状态码的页面，同时还会来到对应的页面
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
