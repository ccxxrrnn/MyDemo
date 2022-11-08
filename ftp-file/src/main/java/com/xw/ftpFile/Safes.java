package com.xw.ftpFile;

import java.util.List;

/**
 * @author xiongwei
 * @description
 */
public class Safes {

    static boolean of(String str){
        return str != null && !"".equals(str);
    }

    static boolean of(List<Object> list){
        return list != null && list.size() != 0;
    }



}
