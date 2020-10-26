package com.learn.blog.service.impl;


import com.learn.blog.bean.Blog;
import com.learn.blog.bean.Tag;
import com.learn.blog.bean.Type;
import com.learn.blog.dao.TagMapper;
import com.learn.blog.exception.NotFoundException;
import com.learn.blog.service.TagService;
import com.learn.blog.utils.CheckUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Tag> listTag() {
        return tagMapper.findAll();
    }


    @Override
    public List<Tag> listTag(String ids) {
        return tagMapper.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        List<Tag> tags = new ArrayList<>(tagMapper.findTop(pageable));
        //只保存已经发表的博客
        for (Tag tag : tags) {
            List<Blog> blogs = tag.getBlogs().stream().filter(blog -> blog.isPublished()).collect(Collectors.toList());
            tag.setBlogs(blogs);
        }
        return tags;
    }

    //解析传递过来的ids
    @Transactional
    public List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (ids != null && !"".equals(ids)) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                if (CheckUtil.checkNum(idarray[i])) {
                    //如果全部是数字,查看是否是数据库中的标签
                    Tag one = tagMapper.getOne(Long.valueOf(idarray[i]));
                    if (one != null) {
                        //如果是则将id加入ids
                        list.add(new Long(idarray[i]));
                    } else {
                        //否在作为新标签插入
                        Tag newtTag = new Tag();
                        newtTag.setName(idarray[i]);
                        tagMapper.save(newtTag);
                        list.add(newtTag.getId());
                    }
                } else {
                    //如果包含非数字，说明是新标签，直接插入即可
                    Tag newtTag = new Tag();
                    newtTag.setName(idarray[i]);
                    tagMapper.save(newtTag);
                    list.add(newtTag.getId());
                }
            }
        }
        return list;
    }


}
