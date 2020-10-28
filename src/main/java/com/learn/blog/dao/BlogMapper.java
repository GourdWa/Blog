package com.learn.blog.dao;

import com.learn.blog.bean.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-11-14:54
 */
public interface BlogMapper extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where  b.published=true and (b.title like ?1 or b.content like ?1)")
    Page<Blog> findByQuery(Pageable pageable, String query);

    //更新浏览次数
    @Transactional
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);

    //返回发布过博客的年份
    @Query("select function('date_format',b.createTime,'%Y') as year from Blog b where b.published=true " +
            "group by function('date_format',b.createTime,'%Y') order by year desc")
    List<String> findGroupByYear();

    @Query("select b from Blog b where function('date_format',b.createTime,'%Y') = ?1 and b.published=true")
    List<Blog> findByYear(String year);
}
