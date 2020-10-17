package com.learn.blog.service.impl;

import com.learn.blog.bean.Type;
import com.learn.blog.dao.TypeMapper;
import com.learn.blog.exception.NotFoundException;
import com.learn.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-08-20:58
 */
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeMapper typeMapper;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeMapper.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeMapper.findById(id).get();
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Type getTypeByName(String name) {
        return typeMapper.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeMapper.findAll(pageable);
    }

    @Override
    public List<Type> list() {
        return typeMapper.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeMapper.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeMapper.findById(id).get();
        if (t == null) {
            throw new NotFoundException("查询的类型（Type）不存在");
        }
        BeanUtils.copyProperties(type, t);
        return typeMapper.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeMapper.deleteById(id);
    }
}
