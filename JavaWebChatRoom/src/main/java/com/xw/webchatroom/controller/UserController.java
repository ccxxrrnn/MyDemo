package com.xw.webchatroom.controller;

import com.xw.webchatroom.NettyServer.MyServer;
import com.xw.webchatroom.entities.CommonResult;
import com.xw.webchatroom.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-17 18:39
 */

@Controller
public class UserController {


    private static Deque<User> users = new ArrayDeque<>();

    @GetMapping("/main")
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("main");
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @PostMapping("/user/index/enter.do")
    @ResponseBody
    public CommonResult<Void> doEnter(
            @RequestParam("userName") String userName,
            HttpSession session) {
        for (User u : users) {
            //存在就跳出
            if (u.getUsername().equals(userName)) {
                return new CommonResult<>(1);
            }
        }
        int id = (int) (Math.random() * 100);
        User user = new User(id, userName);
        users.add(user);
        session.setAttribute("user", user);
        return new CommonResult<>(1);
    }

    @PostMapping("/user/main/renewal.do")
    @ResponseBody
    public CommonResult<Void> doRenewal(ModelMap modelMap){
        modelMap.addAttribute("users",users);
        return new CommonResult<>(1);
    }
}
