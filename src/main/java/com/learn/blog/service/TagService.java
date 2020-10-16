package com.learn.blog.service;


import com.learn.blog.bean.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */
public interface TagService {
    /**
     * 保存标签
     * @param tag
     * @return
     */
    Tag saveTag(Tag tag);

    /**
     * 根据id获得标签
     * @param id
     * @return
     */
    Tag getTag(Long id);

    /**
     * 根据名称获得标签
     * @param name
     * @return
     */
    Tag getTagByName(String name);

    /**
     * 获得标签分页
     * @param pageable
     * @return
     */
    Page<Tag> listTag(Pageable pageable);

    /**
     * 更新标签
     * @param id
     * @param tag
     * @return
     */
    Tag updateTag(Long id, Tag tag);

    /**
     * 删除标签
     * @param id
     */
    void deleteTag(Long id);

    /**
     * 获取所有的标签
     * @return
     */
    List<Tag> listTag();

    /**
     *根据ids查询出所有对应的标签
     *
     */
    List<Tag> listTag(String ids);
    /**
     * 检查标签是否存在，如果没有则保存
     *
     */

}
