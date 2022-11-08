package com.xw.easyExcelExample.writeExample;

import com.alibaba.excel.EasyExcel;
import com.xw.easyExcelExample.write1.entity.User;

/**
 * @author xiongwei
 * @description
 * @date 2022/10/26 15:39
 */
public class ReadDemo {

    public static void main(String[] args) {
        EasyExcel.read("D:\\easyexcel\\writeDemo.xlsx", User.class , new UserReadListen()).sheet().doRead();
    }
}
