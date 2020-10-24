package com.learn.blog.controller;

import com.learn.blog.bean.Comment;
import com.learn.blog.bean.User;
import com.learn.blog.service.BlogService;
import com.learn.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-23-19:45
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;


    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 保存评论逻辑
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        comment.setBlog(blogService.getBlog(comment.getBlog().getId()));
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
            comment.setAdminComment(false);
        }

        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }
}
