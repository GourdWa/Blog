package com.learn.blog.controller.admin;

import com.learn.blog.bean.User;
import com.learn.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author Zixiang Hu
 * @description
 * @create 2020-10-06-19:09
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * 跳转登录页
     *
     * @return
     */
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 登录功能
     * @param username
     * @param password
     * @param httpSession
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
                        HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            // 将user放入Session
            // 不要将密码放入session
            user.setPassword(null);
            httpSession.setAttribute("user", user);
            return "admin/index";
        }

        // RedirectAttributes用于重定向之后还能带参数跳转的的工具类
        //这里相当于将message放在了Session域中
        redirectAttributes.addFlashAttribute("message", "用户名或密码错误");
        // 一定要使用重定向，这样不会携带数据,一定一定要加斜杠
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        // 清空session域
        httpSession.removeAttribute("user");
        // 重定向回登录页面,一定一定要加斜杠
        return "redirect:/admin";
    }
}
