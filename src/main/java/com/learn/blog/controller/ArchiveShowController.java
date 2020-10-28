package com.learn.blog.controller;

import com.learn.blog.bean.Blog;
import com.learn.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-28-21:16
 */
@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model) {
        Map<String, List<Blog>> map = blogService.archiveBlog();
        //获得博客的数目
        Long blogCount = 0L;
        for (List<Blog> value : map.values()) {
            blogCount += value.size();
        }
        model.addAttribute("archiveMap", map);
        model.addAttribute("blogCount", blogCount);
        return "archives";
    }
}
