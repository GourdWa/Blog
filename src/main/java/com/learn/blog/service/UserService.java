package com.learn.blog.service;

import com.learn.blog.bean.User;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-06-19:03
 */
public interface UserService {
    User checkUser(String username, String password);
}
