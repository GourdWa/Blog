package com.learn.blog.service;

import com.learn.blog.bean.Comment;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-23-19:50
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
