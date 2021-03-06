package com.learn.blog.service;

import com.learn.blog.bean.Blog;
import com.learn.blog.ov.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

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
     * 前台展示的Blog获取
     */
    Blog getAndConvert(Long id);

    /**
     * 用于后台查询并分页展示Blog
     *
     * @param pageable
     * @param blogQuery
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    /**
     * 根据tagid分页查询博客
     *
     * @param pageable
     * @param tagId
     * @return
     */
    Page<Blog> listBlogByTagId(Pageable pageable, Long tagId);

    /**
     * 根据tagid分页查询博客
     *
     * @param pageable
     * @param typeId
     * @return
     */
    Page<Blog> listBlogByTypeId(Pageable pageable, Long typeId);

    /**
     * 主要用于前台无条件的分页展示
     *
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Pageable pageable);

    /**
     * 博客前端查询
     */
    Page<Blog> listBlog(Pageable pageable, String query);

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
     * 更新点赞数
     */

    Blog updateGoodJob(Blog blog);

    /**
     * 删除Blog
     *
     * @param id
     */
    void deleteBlog(Long id);

    /**
     * 用于前台首页显示推荐的博客
     */

    List<Blog> listRecommendBlogTop(Integer size);

    /**
     * 对已发布的博客进行归档
     */
    Map<String, List<Blog>> archiveBlog();

}
