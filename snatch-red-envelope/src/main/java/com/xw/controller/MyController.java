package com.xw.controller;

import com.xw.general.entities.CommonResult;
import com.xw.service.MyException;
import com.xw.service.MyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-25 19:17
 */

@Controller
public class MyController {

    @Resource
    MyService myService;

    @PostMapping("/rushToBuy/sendRedPaperLine")
    @ResponseBody
    public CommonResult<Void> sendRedEnvelope(@RequestParam("redId") String redId,
                                              @RequestParam("amount") String amount,
                                              @RequestParam("num") String num){
        //交给service处理，至于数据库就在省略在service处理了，
        System.out.println("发红包请求");
        myService.sendRedEnvelope(redId,amount,num);
        return new CommonResult<>(1);
    }

    @PostMapping("/rushToBuy/getRedPaper")
    @ResponseBody
    public CommonResult<Void> getRedPaper(@RequestParam("redId") String redId,
                                          @RequestParam("userId") String userId){
        //在这里打印数据吧，拿到list集合分配给
        //现实中肯定是每个人都去数据库请求  就不采用上一种方式 return int型 即他的金额 随机拿取
        try {
            double res = myService.getRedPaper(redId,userId);
            return new CommonResult<>(1);
        }catch (MyException | InterruptedException e){
            System.out.println(e.getMessage());
            return new CommonResult<>(0,e.getMessage());
        }
    }
}
