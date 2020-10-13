package com.learn.blog.controller.admin;

import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Type;
import com.learn.blog.bean.User;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-07-18:32
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs_input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    @Autowired
    private BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blogQuery,
                       Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("types", typeService.list());
        return LIST;
    }

    /**
     *单独定义搜索函数的目的是让thymeleaf只渲染片段，导航栏和底部不需要做渲染
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blogQuery,
                         Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        //返回admin/blogs中的blogList片段,实现局部渲染
        return "admin/blogs :: blogList";
    }

    /**
     * 来到博客发布页面
     *
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.list());
        model.addAttribute("tags", tagService.list());
        return INPUT;
    }

    /**
     * 提交新增的博客
     *
     * @return
     */
    @PostMapping("/blogs")
    public String publish(@Valid Blog blog, BindingResult bindingResult,
                          HttpSession httpSession,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "请检查博客标题、内容、首图是否为空");
            return INPUT;
        }
        //设置用户
        blog.setUser((User) httpSession.getAttribute("user"));
        //因为前端传回的Blog对象中的Type属性仅有id这个字段有值
        blog.setType(typeService.getType(blog.getType().getId()));
        //设置标签，前端传回的是‘1，2，3’
        blog.setTags(tagService.listTags(blog.getTagIds()));
        Blog saveBlog = blogService.saveBlog(blog);
        if (saveBlog == null) {
            redirectAttributes.addFlashAttribute("message", "新增失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "新增成功");
        }
        //提交成功后返回列表页面
        return REDIRECT_LIST;
    }
}
