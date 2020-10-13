package com.learn.blog.service.impl;


import com.learn.blog.bean.Tag;
import com.learn.blog.dao.TagMapper;
import com.learn.blog.exception.NotFoundException;
import com.learn.blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagMapper.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagMapper.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagMapper.findAll(pageable);
    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagMapper.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        return tagMapper.save(t);
    }


    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagMapper.deleteById(id);
    }

    @Override
    public List<Tag> list() {
        return tagMapper.findAll();
    }


    @Override
    public List<Tag> listTags(String ids) {
        return tagMapper.findAllById(convertToList(ids));
    }

    //解析传递过来的ids
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
