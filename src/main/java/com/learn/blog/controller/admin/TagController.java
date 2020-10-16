package com.learn.blog.controller.admin;

import com.learn.blog.bean.Tag;
import com.learn.blog.bean.Type;
import com.learn.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-10-21:15
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    @Autowired
    HttpSession httpSession;

    /**
     * 分页列表展示,id倒序,Pageable默认情况下page是0，也就展示第一页的数据
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageable, Model model, @RequestParam(name = "deleteName", required = false) String deleteName) {
        if (deleteName != null && !"".equals(deleteName))
            model.addAttribute("message", "【" + deleteName + "】删除成功");
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    /**
     * 跳转到新增标签页面
     */
    @GetMapping("/tags/input")
    public String input() {
        return "admin/tags_input";
    }

    /**
     * 验证标签保存逻辑
     */
    @PostMapping("/tags")
    public String save(@Valid Tag tag, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        //去除两边的空格
        tag.setName(tag.getName().trim());
        //后端校验
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "提交信息不符合合规则");
            return "admin/tags_input";
        }
        Tag tagByName = tagService.getTagByName(tag.getName());
        //说明已经存在该标签
        if (tagByName != null) {
            model.addAttribute("message", "标签名称已经存在");
            return "admin/tags_input";
        }
        Tag saveTag = tagService.saveTag(tag);
        if (saveTag == null) {
            redirectAttributes.addFlashAttribute("message", "标签保存失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "标签保存成功");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 修改标签跳转页面
     */

    @GetMapping("tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("name", tag.getName());
        model.addAttribute("id", id);
        return "admin/tags_edit";
    }

    /**
     * 实现标签修改逻辑
     */
    @PostMapping("/tags/update")
    public String updateTag(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //如果校验有错误
        if (bindingResult.hasErrors()) {
            //返回之前的页面
            model.addAttribute("message", "提交信息不符合合规则");
            return "admin/tags_input";
        }
        Tag tagByName = tagService.getTagByName(tag.getName().trim());
        //判断分类的名称与已经存在的名称是否冲突
        if (tagByName != null && tag.getId() != tagByName.getId()) {
            model.addAttribute("message", "分类名称已经存在");
            return "admin/tags_input";
        }

        Tag updateTag = tagService.updateTag(tag.getId(), tag);
        if (updateTag == null) {
            redirectAttributes.addFlashAttribute("message", "标签名称修改失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "标签名称修改成功");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     */
    @GetMapping("/tags/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}
