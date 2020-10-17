package com.learn.blog.service;

import com.learn.blog.bean.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-08-20:49
 */
public interface TypeService {
    /**
     * 新增类型
     *
     * @param type
     * @return
     */
    Type saveType(Type type);

    /**
     * 根据id获得类型
     *
     * @param id
     * @return
     */
    Type getType(Long id);

    /**
     * 通过名称获得Type
     */
    Type getTypeByName(String name);

    /**
     * 分页形式，获得所有类型
     *
     * @param pageable
     * @return
     */
    Page<Type> listType(Pageable pageable);

    /**
     * 获得所有类型
     *
     * @return
     */
    List<Type> list();

    /**
     *主要用于前台显示
     */

    List<Type> listTypeTop(Integer size);

    /**
     * 更新类型
     *
     * @param id
     * @param type
     * @return
     */
    Type updateType(Long id, Type type);

    /**
     * 删除一个类型
     *
     * @param id
     */
    void deleteType(Long id);
}
