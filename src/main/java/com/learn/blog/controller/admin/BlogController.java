package com.learn.blog.controller.admin;

import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Type;
import com.learn.blog.ov.BlogQuery;
import com.learn.blog.service.BlogService;
import com.learn.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-07-18:32
 */
@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    TypeService typeService;

    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blogQuery,
                       Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("types", typeService.list());
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blogQuery,
                         Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        //返回admin/blogs中的blogList片段,实现局部渲染
        return "admin/blogs :: blogList";
    }
}
