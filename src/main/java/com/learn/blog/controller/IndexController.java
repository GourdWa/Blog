package com.learn.blog.controller;

import com.learn.blog.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-09-10-21:08
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
//        int i = 9 / 0;
// ----------------------------------
/*        String blog = null;
        if (blog == null) {
            throw new NotFoundException("博客首页找不到");
        }*/
        System.out.println("-----index-----");
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
//        int i = 9 / 0;
// ----------------------------------
/*        String blog = null;
        if (blog == null) {
            throw new NotFoundException("博客首页找不到");
        }*/
        System.out.println("-----blog-----");
        return "admin/login";
    }
}
