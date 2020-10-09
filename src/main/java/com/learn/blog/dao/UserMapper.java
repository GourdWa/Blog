package com.learn.blog.dao;

import com.learn.blog.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-06-19:05
 */
public interface UserMapper extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);
}
