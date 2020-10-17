package com.learn.blog.dao;

import com.learn.blog.bean.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-11-14:54
 */
public interface BlogMapper extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);
}
