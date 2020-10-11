package com.learn.blog.dao;

import com.learn.blog.bean.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-10-21:07
 */
public interface TagMapper extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
