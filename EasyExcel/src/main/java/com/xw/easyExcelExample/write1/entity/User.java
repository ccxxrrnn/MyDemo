package com.xw.easyExcelExample.write1.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiongwei
 * @description
 * @date 2022/10/26 15:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ExcelProperty({"用户名"})
    String userName;

    @ExcelProperty({"性别"})
    String sex;
}
