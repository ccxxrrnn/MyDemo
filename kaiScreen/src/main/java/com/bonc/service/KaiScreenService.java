package com.bonc.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 14:03
 */


public interface KaiScreenService {

    /**
     *  通过时间返回当前时间的门诊排名list
     * @param nowTime
     * @return
     */
    JSONObject showOutpatientsRankListByDate(String nowTime);

    /**
     * 根据医院的id查询医院的门诊当前时间的详细信息
     * @param hospitalId
     * @param nowTime
     * @return
     */
    JSONObject showOutpatientsInDetailByHospitalIdAndDate(String hospitalId , String nowTime);

    /**
     * 展示门诊相关门诊人数和实时信息
     * @param nowTime
     * @return
     */
    JSONObject showOutpatientsPopulationByDate(String nowTime);
}
