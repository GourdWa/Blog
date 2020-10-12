package com.learn.blog.service;

import com.learn.blog.bean.Blog;
import com.learn.blog.ov.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-11-14:50
 */
public interface BlogService {
    /**
     * 查询一个Blog
     *
     * @param id
     * @return
     */
    Blog getBlog(Long id);

    /**
     * 查询并分页展示Blog
     *
     * @param pageable
     * @param blogQuery
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    /**
     * 新增Blog
     *
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 更新Blog
     *
     * @param id
     * @param blog
     * @return
     */
    Blog updateBlog(Long id, Blog blog);

    /**
     * 删除Blog
     *
     * @param id
     */
    void deleteBlog(Long id);
}
