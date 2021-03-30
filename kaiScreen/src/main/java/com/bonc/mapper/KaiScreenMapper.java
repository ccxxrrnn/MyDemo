package com.bonc.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 14:09
 */

@Mapper
public interface KaiScreenMapper {

    /**
     * 返回医院排名map对象
     * @return
     */
    List<Map<String,Object>> selectOutpatientsRankList(String nowTime);

    /**
     *返回实时门诊分布情况 map对象
     * @return
     */
    List<Map<String,Object>> selectOutpatientsPieData(String nowTime);


    /**
     * 返回分时段趋势图数据
     * @param hospitalId
     * @return
     */
    List<Map<String,Object>> selectOutpatientsLineData(String hospitalId);

    /**
     * 返回门诊累计统计表数据
     * @param hospitalId
     * @param nowTime
     * @return
     */
    List<Map<String,Object>> selectOutpatientsTableData(String hospitalId , String nowTime);

    /**
     * 根据时间返回当前时间，门诊的人数
     * @param nowTime
     * @return
     */
    Integer selectOutpatientsPopulationByTime(String nowTime);

    /**
     * 搜索所有门诊医院名和所有部门和对应的人数
     * @return
     */
    List<Map<String,Object>> selectOutpatientsHospital(String nowTime);

}
