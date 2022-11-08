package com.xw.easyExcelExample.write3;

import com.alibaba.excel.EasyExcel;

/**
 * @author xiongwei
 * @description 针对于追加，多次写入的情况
 * @date 2022/10/26 17:09
 */
public class ManyWriteDemo {

    public static void main(String[] args) {
        String fileName = "D:\\easyexcel\\ManyWriteDemo.xlsx";
        EasyExcel.write(fileName).sheet();
    }
}
