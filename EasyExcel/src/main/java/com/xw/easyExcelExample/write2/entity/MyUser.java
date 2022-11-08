package com.xw.easyExcelExample.write2.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUser {

    @ExcelProperty({"大标题","用户名"})
    String userName;

    @ExcelProperty(value = {"大标题","性别"} , index = 3)
    String sex;
}

