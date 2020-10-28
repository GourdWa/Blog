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

import javax.persistence.criteria.*;
import java.util.*;

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

    @Transactional
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
        //更新浏览次数
        blogMapper.updateViews(id);
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
    public Page<Blog> listBlogByTagId(Pageable pageable, Long tagId) {

        return blogMapper.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                Join<Object, Object> join = root.join("tags");
                predicates.add(criteriaBuilder.equal(join.get("id"), tagId));
                predicates.add(criteriaBuilder.equal(root.get("published"), true));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlogByTypeId(Pageable pageable, Long typeId) {
        return blogMapper.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                ArrayList<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("type").get("id"), typeId));
                predicates.add(criteriaBuilder.equal(root.get("published"), true));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        //只返回已经发布的博客
        Page<Blog> blogs = blogMapper.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("published"), true);
            }
        }, pageable);
        return blogs;
//        return blogMapper.findAll(pageable);
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
        List<Blog> blogList = blogMapper.findTop(pageable);
        List<Blog> blogs = new ArrayList<>();
        for (Blog blog : blogList) {
            if (blog.isPublished())
                blogs.add(blog);
        }

        return blogs;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogMapper.findGroupByYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            List<Blog> blogs = blogMapper.findByYear(year);
            map.put(year, blogs);
        }
        return map;
    }
}
