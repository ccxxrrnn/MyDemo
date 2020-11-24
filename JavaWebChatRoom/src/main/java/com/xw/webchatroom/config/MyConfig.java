package com.xw.webchatroom.config;

import com.xw.webchatroom.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-22 19:38
 */

@Configuration
public class MyConfig implements WebMvcConfigurer {

    //所有的WebMvcConfigurer都会生效
    @Bean //将组件注册在容器
    public WebMvcConfigurer  webMvcConfigurer(){
        WebMvcConfigurer config=new WebMvcConfigurer(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("login");
            }

//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**");
////                        .excludePathPatterns("/","/login.html","/user/login");
//            }
        };
        return config;
    }

}
