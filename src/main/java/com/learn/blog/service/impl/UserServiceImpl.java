package com.learn.blog.service.impl;

import com.learn.blog.bean.User;
import com.learn.blog.dao.UserMapper;
import com.learn.blog.service.UserService;
import com.learn.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-06-19:04
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        // 数据库中存储的是经过md5加密的
        User user = userMapper.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
