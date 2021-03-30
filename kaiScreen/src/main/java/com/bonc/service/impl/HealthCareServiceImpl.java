package com.bonc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.mapper.HealthCareMapper;
import com.bonc.service.HealthCareService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-25 14:05
 */

@Service
public class HealthCareServiceImpl implements HealthCareService {

    @Resource
    HealthCareMapper healthCareMapper;

    /**
     *展示医院购药页面相关信息
     *
     * @param nowDate
     * @return
     */
    @Override
    public JSONObject showMedicalStatisticsByDate(String nowDate) {
        //医院购药统计的所有数据
        Map<String,Object> allData = new LinkedHashMap<>();
        //热门药店排名
        Map<String,Object> tableDataOne = new LinkedHashMap<>();
        //表头
        List<String> thead1 = new ArrayList<>();
        thead1.add("排名");
        thead1.add("药店");
        thead1.add("用户数");
        tableDataOne.put("thead",thead1);
        //表数据
        List<List<String>> tbody1 = new ArrayList<>();
        //返回医院排名相关信息
        List<Map<String,Object>> tbodyDataList1 = healthCareMapper.selectHospitalRank(nowDate);
        for (Map map : tbodyDataList1){
            List<String> list = new ArrayList<>();
            list.add((String) map.get("rank"));
            list.add((String) map.get("hosName"));
            list.add(map.get("peoNums").toString());
            tbody1.add(list);
        }
        tableDataOne.put("tbody",tbody1);
        //如果结果体为空即无数据就不添加
        if (!tbody1.isEmpty()) {
            allData.put("tableDataOne", tableDataOne);
        }
        //热门药品排名
        Map<String,Object> tableDataTwo = new LinkedHashMap<>();
        List<String> thead2 = new ArrayList<>();
        thead2.add("排名");
        thead2.add("药品");
        thead2.add("用户数");
        tableDataTwo.put("thead",thead2);
        List<List<String>> tbody2 = new ArrayList<>();
        //返回药品排名相关信息
        List<Map<String,Object>> tbodyDataList2 = healthCareMapper.selectMedicalRank(nowDate);
        for (Map map : tbodyDataList2){
            List<String> list = new ArrayList<>();
            list.add((String) map.get("rank"));
            list.add((String) map.get("medName"));
            list.add(map.get("peoNums").toString());
            tbody2.add(list);
        }
        tableDataTwo.put("tbody",tbody2);
        if (!tbody2.isEmpty()) {
            allData.put("tableDataTwo", tableDataTwo);
        }
        //处方类型所占比和总数
        Map<String,Object> chartDataMap = healthCareMapper.selectPrescriptionData(nowDate);
        if (!chartDataMap.isEmpty()) {
            List<Integer> chartData = new ArrayList<>();
            String otcStr = chartDataMap.get("otc").toString();
            int otc = Integer.parseInt(otcStr.substring(0, otcStr.length() - 1));
            String rxStr = chartDataMap.get("rx").toString();
            int rx = Integer.parseInt(rxStr.substring(0, rxStr.length() - 1));
            chartData.add(rx);
            chartData.add(otc);
            allData.put("chartData", chartData);
            allData.put("chartDataTotal", ((BigDecimal) chartDataMap.get("total")).intValue());
        }
        //药品维度所占比和总数
        Map<String,Object> pieDataMap = healthCareMapper.selectMedicinalData(nowDate);
        if (!pieDataMap.isEmpty()) {
            List<Integer> pieData = new ArrayList<>();
            String wmStr = pieDataMap.get("wm").toString();
            int wm = Integer.parseInt(wmStr.substring(0, wmStr.length() - 1));
            String tcmStr = pieDataMap.get("tcm").toString();
            int tcm = Integer.parseInt(tcmStr.substring(0, tcmStr.length() - 1));
            String cmStr = pieDataMap.get("cm").toString();
            int cm = Integer.parseInt(cmStr.substring(0, cmStr.length() - 1));
            pieData.add(wm);
            pieData.add(tcm);
            pieData.add(cm);
            allData.put("pieData", pieData);
            allData.put("pieDataTotal", ((BigDecimal) pieDataMap.get("total")).intValue());
        }
        List<Map<String,Object>> barData = new ArrayList<>();
        //购药时间分布柱状图
        List<Map<String,Object>> barDataList = healthCareMapper.selectMedicalBuyDistribution(nowDate);
        //x坐标,当前时间
        List<Integer> dataX = new ArrayList<>();
        //y坐标,人数
        List<Integer> numData = new ArrayList<>();
        for (Map map : barDataList){
            dataX.add(Integer.parseInt(map.get("dataX").toString()));
            numData.add(((BigDecimal)map.get("chartData")).intValue());
        }
        Map<String,Object> barData1 = new LinkedHashMap<>();
        //当x坐标和y坐标都不为空时才赋值
        if (!(dataX.isEmpty() || numData.isEmpty())) {
            barData1.put("dataX", dataX);
            barData1.put("chartData", numData);
        }
        barData.add(barData1);
        if (!barData.isEmpty()) {
            allData.put("barData", barData);
        }
        JSONObject json = new JSONObject(allData);
        return json;
    }
}
