package com.learn.blog.controller;

import com.learn.blog.bean.Blog;
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
import org.springframework.web.bind.annotation.*;

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
        //列举出博客引用数最多的前几个类型
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable(name = "id") Long id, Model model) {
        //需要将博客的内容转换为html
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    /**
     * 博客查询，主要是查询博客标题和内容
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model, @RequestParam(name = "query") String query) {
        //前端展示的查询
        model.addAttribute("page", blogService.listBlog(pageable, "%" + query + "%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/updateGoodjob")
    @ResponseBody
    public void goodJob(@RequestParam(name = "id") Long id) {
        Blog blog = blogService.getBlog(id);
        blog.setGoodJob(blog.getGoodJob() + 1);
        blogService.updateGoodJob(blog);
    }

}
