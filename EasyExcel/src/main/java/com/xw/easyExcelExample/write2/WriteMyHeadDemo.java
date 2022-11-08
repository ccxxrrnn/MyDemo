package com.xw.easyExcelExample.write2;

import com.alibaba.excel.EasyExcel;
import com.xw.easyExcelExample.write2.entity.MyUser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongwei
 * @description 自定义表头 【   a  】
 *                       【b】【c】
 * @date 2022/10/26 16:37
 */
public class WriteMyHeadDemo {
    public static void main(String[] args) {
        String fileName = "D:\\easyexcel\\WriteMyHeadDemo.xlsx";
        List<MyUser> userList = new ArrayList<>();
        userList.add(new MyUser("xw" , "男"));
        userList.add(new MyUser("测试" , "女"));
        EasyExcel.write(fileName).head(MyUser.class).sheet().doWrite(userList);
    }
}
