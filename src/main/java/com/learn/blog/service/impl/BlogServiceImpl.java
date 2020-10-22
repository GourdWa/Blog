package com.learn.blog.service.impl;

import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Type;
import com.learn.blog.dao.BlogMapper;
import com.learn.blog.exception.NotFoundException;
import com.learn.blog.ov.BlogQuery;
import com.learn.blog.service.BlogService;
import com.learn.blog.utils.MarkDownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-11-14:53
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Blog getBlog(Long id) {
        return blogMapper.getOne(id);
    }

    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogMapper.getOne(id);
        if (blog == null) {
            throw new NotFoundException("博客不存在");
        }
        Blog newBlog = new Blog();
        BeanUtils.copyProperties(blog, newBlog);
        String content = newBlog.getContent();
        String htmlContent = MarkDownUtil.markdownToHtmlExtensions(content);
        newBlog.setContent(htmlContent);
        return newBlog;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return blogMapper.findAll((Specification<Blog>) (root, query, criteriaBuilder) -> {
            ArrayList<Predicate> predicates = new ArrayList<>();
            if (blogQuery.getTitle() != null && !"".equals(blogQuery.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + blogQuery.getTitle() + "%"));
            }
            if (blogQuery.getTypeId() != null) {
                predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
            }
            if (blogQuery.isRecommend()) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
            }
            query.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogMapper.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {

        return blogMapper.findByQuery(pageable, query);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        blog.setGoodJob(0);

        return blogMapper.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogMapper.getOne(id);
        if (blog1 == null) {
            throw new NotFoundException("该博客不存在");
        }
        //设置博客的更新时间和创建时间
        blog.setUpdateTime(new Date());
        blog.setCreateTime(blog1.getCreateTime());
        blog.setViews(blog1.getViews());
        blog.setGoodJob(blog1.getGoodJob());

        BeanUtils.copyProperties(blog, blog1);
        return blogMapper.save(blog1);
    }

    @Override
    public Blog updateGoodJob(Blog blog) {
        return blogMapper.save(blog);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogMapper.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogMapper.findTop(pageable);
    }
}
