package com.learn.blog.controller.admin;

import com.learn.blog.bean.Type;
import com.learn.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.Year;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-09-19:23
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    /**
     * 分页列表展示,id倒序,Pageable默认情况下page是0，也就展示第一页的数据
     */
    @GetMapping("/types")
    public String list(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model, @RequestParam(name = "deleteName", required = false) String deleteName) {
        if (deleteName!=null&&!"".equals(deleteName))
            model.addAttribute("message", "成功删除【" + deleteName + "】");
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 新增类型逻辑
     */
    @PostMapping("/types")
    public String save(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        type.setName(type.getName().trim());
        //如果校验有错误
        if (bindingResult.hasErrors()) {
            //返回之前的页面
            model.addAttribute("message", "提交信息不符合合规则");
            return "admin/types_input";
        }
        Type byName = typeService.getTypeByName(type.getName());
        if (byName != null) {
            model.addAttribute("message", "分类名称已经存在");
            return "admin/types_input";
        }
        Type sType = typeService.saveType(type);
        if (sType == null) {
            redirectAttributes.addFlashAttribute("message", "分类保存失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "分类保存成功");
        }
        //再返回分页列表展示页面
        return "redirect:/admin/types";
    }

    /**
     * 输入新增类型
     */
    @GetMapping("/types/input")
    public String input() {
        return "admin/types_input";
    }

    /**
     * 修改分类，获取分类的id和名称
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model) {
        Type type = typeService.getType(id);
        model.addAttribute("name", type.getName());
        model.addAttribute("id", id);
        return "admin/types_edit";
    }

    /**
     * 修改分类实现
     */
    @PostMapping("/types/update")
    public String updateType(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //如果校验有错误
        if (bindingResult.hasErrors()) {
            //返回之前的页面
            model.addAttribute("message", "提交信息不符合合规则");
            return "admin/types_input";
        }
        Type typeByName = typeService.getTypeByName(type.getName().trim());
        //判断分类的名称与已经存在的名称是否冲突
        if (typeByName != null && type.getId() != typeByName.getId()) {
            model.addAttribute("message", "分类名称已经存在");
            return "admin/types_input";
        }

        Type sType = typeService.updateType(type.getId(), type);
        if (sType == null) {
            redirectAttributes.addFlashAttribute("message", "分类名称修改失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "分类名称修改成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 删除逻辑
     */
    @GetMapping("/types/{id}/delete")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        typeService.deleteType(id);
//        redirectAttributes.addFlashAttribute("message", "分类删除成功");
//        return "redirect:/admin/types";
    }

}
