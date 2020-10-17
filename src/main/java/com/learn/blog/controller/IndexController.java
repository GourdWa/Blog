package com.learn.blog.controller;

import com.learn.blog.exception.NotFoundException;
import com.learn.blog.ov.BlogQuery;
import com.learn.blog.service.BlogService;
import com.learn.blog.service.TagService;
import com.learn.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-09-10-21:08
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        //获得分页的数据
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        System.out.println("-----blog-----");
        return "admin/login";
    }
}
