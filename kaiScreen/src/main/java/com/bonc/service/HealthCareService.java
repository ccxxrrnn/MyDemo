package com.bonc.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-25 14:05
 */

public interface HealthCareService {

    /**
     *返回当前时间药品统计数据
     * @param nowDate
     * @return
     */
    JSONObject showMedicalStatisticsByDate(String nowDate);
}
