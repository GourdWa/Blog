package com.learn.blog.controller.admin;

import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Type;
import com.learn.blog.bean.User;
import com.learn.blog.ov.BlogQuery;
import com.learn.blog.service.BlogService;
import com.learn.blog.service.TagService;
import com.learn.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

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
    public String list(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blogQuery,
                       Model model, @RequestParam(name = "deleteName", required = false) String deleteName) {
        if (deleteName != null && !"".equals(deleteName))
            model.addAttribute("message", "【" + deleteName + "】删除成功");
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("types", typeService.list());
        return LIST;
    }

    /**
     * 单独定义搜索函数的目的是让thymeleaf只渲染片段，导航栏和底部不需要做渲染
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blogQuery,
                         Model model) {
        Page<Blog> blogs = blogService.listBlog(pageable, blogQuery);
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
        model.addAttribute("tags", tagService.listTag());
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

        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog saveBlog = blogService.saveBlog(blog);
        if (saveBlog == null) {
            redirectAttributes.addFlashAttribute("message", "新增失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "新增成功");
        }
        //提交成功后返回列表页面
        return REDIRECT_LIST;
    }

    /**
     * 编辑博客，来到博客编辑页面
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable(name = "id") Long id, Model model) {
        Blog blog = blogService.getBlog(id);
        //将tagIds初始化
        blog.initTagIds();
        model.addAttribute("blog", blog);
        model.addAttribute("types", typeService.list());
        model.addAttribute("tags", tagService.listTag());
        return "admin/blogs_edit";
    }

    /**
     * 处理更新逻辑
     *
     * @return
     */
    @PostMapping("/blogs/update")
    public String update(@Valid Blog blog, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, HttpSession httpSession,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "请检查博客标题、内容、首图是否为空");
            return "redirect:/blogs/" + blog.getId() + "/input";
        }
        //修改更新时间和id，因为传递过来的id是1,2,3这样形式的tagIds，因此需要对其进行解析再保存
        blog.setTags(tagService.listTag(blog.getTagIds()));
        //设置用户
        User user = (User) httpSession.getAttribute("user");
        blog.setUser(user);
        Blog updateBlog = blogService.updateBlog(blog.getId(), blog);
        if (updateBlog == null) {
            redirectAttributes.addFlashAttribute("message", "修改失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改成功");
        }
        return REDIRECT_LIST;
    }

    /**
     * 处理删除
     */
    @GetMapping("/blogs/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable(name = "id") Long id) {
        blogService.deleteBlog(id);
    }

}
