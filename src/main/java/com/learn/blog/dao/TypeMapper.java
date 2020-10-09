package com.learn.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.blog.bean.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-08-20:58
 */
public interface TypeMapper extends JpaRepository<Type, Long> {
}
