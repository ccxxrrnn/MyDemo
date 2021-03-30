package com.bonc.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-25 14:33
 */

@Mapper
public interface HealthCareMapper {

    /**
     * 返回当前时间的医院排名信息
     * @param nowDate
     * @return
     */
    List<Map<String, Object>> selectHospitalRank(String nowDate);

    /**
     * 返回当前时间的药品排名信息
     * @param nowDate
     * @return
     */
    List<Map<String, Object>> selectMedicalRank(String nowDate);

    /**
     * 搜索当前时间处方占比和总处方数
     * @param nowDate
     * @return
     */
    Map<String, Object> selectPrescriptionData(String nowDate);

    /**
     * 返回时间下药品维度占比和总数
     * @param nowDate
     * @return
     */
    Map<String, Object> selectMedicinalData(String nowDate);

    /**
     * 查询当前日期各时间段购买药品人数
     * @param nowDate
     * @return
     */
    List<Map<String,Object>> selectMedicalBuyDistribution(String nowDate);
}
