package com.learn.blog.dao;

import com.learn.blog.bean.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-23-19:52
 */
public interface CommentMapper extends JpaRepository<Comment, Long> {
    /**
     * 查找顶级节点
     * @param blogId
     * @param sort
     * @return
     */
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
