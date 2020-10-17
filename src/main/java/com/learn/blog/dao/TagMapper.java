package com.learn.blog.dao;

import com.learn.blog.bean.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-10-21:07
 */
public interface TagMapper extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
