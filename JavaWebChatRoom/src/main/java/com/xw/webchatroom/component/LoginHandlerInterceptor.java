package com.xw.webchatroom.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆检查
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user=request.getSession().getAttribute("username");
        if(user==null){
            //这是未登陆，返回登陆界面
//            request.setAttribute("msg","没有权限请先登陆");
            request.getRequestDispatcher("/login.html").forward(request,response);
            return false;
        }
        else{
            //已登录，放行
            return true;
        }
    }

}
