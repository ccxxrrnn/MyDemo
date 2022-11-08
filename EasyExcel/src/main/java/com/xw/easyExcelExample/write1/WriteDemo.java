package com.xw.easyExcelExample.write1;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xw.easyExcelExample.write1.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiongwei
 * @description 普通标头   a b c d
 * @date 2022/10/26 15:42
 */
public class WriteDemo {

    public static void main(String[] args) {
        String fileName = "D:\\easyexcel\\writeDemo.xlsx";
        List<User> userList = new ArrayList<>();
        userList.add(new User("xw" , "男"));
        userList.add(new User("测试" , "女"));
        EasyExcel.write(fileName , User.class).head(User.class).sheet().doWrite(userList);
    }

}
