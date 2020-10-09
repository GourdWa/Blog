package com.learn.blog.controller.admin;

import com.learn.blog.bean.Type;
import com.learn.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String list(@PageableDefault(size = 2, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 新增类型
     */
    @PostMapping("/types")
    public String save(Type type) {
        Type sType = typeService.saveType(type);
        if (sType == null) {

        } else {

        }
        //再返回分页列表展示页面
        return "redirect:/admin/types";
    }

    /**
     * 新增类型
     */
    @GetMapping("/types/input")
    public String input() {
        return "admin/types_input";
    }
}
