package com.learn.blog.service.impl;

import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Type;
import com.learn.blog.dao.BlogMapper;
import com.learn.blog.exception.NotFoundException;
import com.learn.blog.ov.BlogQuery;
import com.learn.blog.service.BlogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;

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

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogMapper.deleteById(id);
    }
}
