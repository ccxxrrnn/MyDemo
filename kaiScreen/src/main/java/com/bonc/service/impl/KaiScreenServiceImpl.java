package com.bonc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bonc.mapper.KaiScreenMapper;
import com.bonc.service.KaiScreenService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 14:08
 */

@Service
public class KaiScreenServiceImpl implements KaiScreenService {

    @Resource
    KaiScreenMapper kaiScreenMapper;

    /**
     * 返回医院排名表和门诊分布情况表
     * @param nowTime
     * @return
     */
    @Override
    public JSONObject showOutpatientsRankListByDate(String nowTime) {
        Map<String,Object> allData = new LinkedHashMap<>();
        //返回医院排名表内容,包括医院名称,医院id和排名
        List<Map<String,Object>> outpatientsRankMap = kaiScreenMapper.selectOutpatientsRankList(nowTime);
        List<Object> rankList = new ArrayList<>();
        //排名表内的数据
        Map<String,Object> rankData = new LinkedHashMap<>();
        //医院名称
        List<String> hospitalNameList = new ArrayList<>();
        //医院id
        List<String> hospitalId = new ArrayList<>();
        //医院排名
        List<Integer> hospitalRank = new ArrayList<>();
        for (Map map : outpatientsRankMap){
            hospitalNameList.add((String) map.get("hospitalNameList"));
            hospitalId.add((String) map.get("hospitalId"));
            hospitalRank.add(((BigDecimal)map.get("hospitalRank")).intValue());
        }
        rankData.put("hospitalNameList",hospitalNameList);
        rankData.put("hospitalId",hospitalId);
        rankData.put("hospitalRank",hospitalRank);
        rankList.add(rankData);
        //空值不添加
        if (!rankData.containsValue(null)) {
            allData.put("rankList", rankList);
        }
        //返回重点门诊科室的名称和占比
        List<Map<String,Object>> PieDataList = kaiScreenMapper.selectOutpatientsPieData(nowTime);
        //重点门诊科室分布情况
        Map<String,Object> pieData = new LinkedHashMap<>();
        //科室名
        List<String> dataX = new ArrayList<>();
        //占比
        List<Double> chartData = new ArrayList<>();
        for (Map map : PieDataList){
            dataX.add((String) map.get("dataX"));
            String str = map.get("chartData").toString();
            Double pre = Double.valueOf(str.substring(0, str.length() - 1));
            chartData.add(pre);
        }
        pieData.put("dataX", dataX);
        pieData.put("chartData", chartData);
        if (!pieData.containsValue(null)) {
            allData.put("pieData", pieData);
        }
        JSONObject json = new JSONObject(allData);
        return json;
    }

    /**
     * 门诊分时段趋势和门诊累计统计
     * @param hospitalId
     * @param nowTime
     * @return
     */
    @Override
    public JSONObject showOutpatientsInDetailByHospitalIdAndDate(String hospitalId , String nowTime) {
        Map<String,Object> allData = new LinkedHashMap<>();
        //返回时间点和对应的时间段接诊的人数
        List<Map<String,Object>> lineDataList = kaiScreenMapper.selectOutpatientsLineData(hospitalId);
        //分时段趋势图
        Map<String,Object> lineData = new LinkedHashMap<>();
        //时间点
        List<Integer> dataX = new ArrayList<>();
        //对应的接诊的人数
        List<Integer> chartData = new ArrayList<>();
        for (Map map : lineDataList){
            dataX.add(Integer.parseInt((String) map.get("dataX")) );
            chartData.add(((BigDecimal)map.get("chartData")).intValue());
        }
        lineData.put("dataX",dataX);
        lineData.put("chartData",chartData);
        if (!lineData.containsValue(null)) {
            allData.put("lineData", lineData);
        }
        //门诊累计统计表
        Map<String,Object> tableData = new LinkedHashMap<>();
        //表头
        List<String> thead = new ArrayList<>();
        thead.add("科室");
        thead.add("人数");
        thead.add("占比");
        tableData.put("thead",thead);
        //表数据
        List<List<String>> tbody = new ArrayList<>();
        //返回科室名和人数,对应的占比;
        List<Map<String,Object>> tbodyDataList = kaiScreenMapper.selectOutpatientsTableData(hospitalId , nowTime);
        for (Map map : tbodyDataList){
            //每一个list是一组数据
            List<String> list = new ArrayList<>();
            list.add((String) map.get("depName"));
            list.add(map.get("nums").toString());
            list.add((String) map.get("proper"));
            tbody.add(list);
        }
        tableData.put("tbody",tbody);
        if (!tbody.isEmpty()) {
            allData.put("tableData", tableData);
        }
        JSONObject json = new JSONObject(allData);
        return json;
    }

    /**
     *当日参加门诊的人数和实时的重点信息
     * @param nowTime
     * @return
     */
    @Override
    public JSONObject showOutpatientsPopulationByDate(String nowTime) {
        Map<String,Object> allData = new LinkedHashMap<>();
        //参与的人数,包括当前分钟的人数和下一分钟的数据
        List<Integer> population = new ArrayList<>();
        String nowMin = "201803230000";
        //一系列操作，运用data日期直接加一分钟，0001数据库无数据
        String nextMin = "201803230010";
        //返回时间段参与门诊的人数
        population.add(kaiScreenMapper.selectOutpatientsPopulationByTime(nowMin));
        population.add(kaiScreenMapper.selectOutpatientsPopulationByTime(nextMin));
        if (!population.isEmpty()) {
            allData.put("population", population);
        }
        //实时重点信息数据
        List<Map<String,Object>> realTimeInf = new ArrayList<>();
        //返回当前时间，医院名，科室名，人数
        List<Map<String,Object>> realTimeInfHospitals = kaiScreenMapper.selectOutpatientsHospital(nowTime);
//        //对照重点信息数据 医院名，实时信息
//        Map<String,Map<String,Object>> contrastRealTimeInf = new LinkedHashMap<>();
        List<Map<String,Object>> res = new ArrayList<>();
        for (Map map : realTimeInfHospitals){
            //日期
            String date = nowTime.substring(0,4) + "/" + nowTime.substring(4,6) + "/" + nowTime.substring(6,8) ;
            //医院名
            String hospitalName = map.get("hospitalName").toString();
            //科室名
            String department = map.get("department").toString();
            //人数
            String num = map.get("num").toString() + "人";
            //添加新的数据
            Map<String, Object> outpatientsPopulationInfo = new LinkedHashMap<>();
            outpatientsPopulationInfo.put("date", date);
            outpatientsPopulationInfo.put("hospitalName", hospitalName);
            outpatientsPopulationInfo.put("department", department);
            outpatientsPopulationInfo.put("num", num);
            realTimeInf.add(outpatientsPopulationInfo);
        }
        Stream<Map<String,Object>> stream = realTimeInf.stream();
        Map<String, List<Map<String, Object>>> map = stream.collect(Collectors.groupingBy(e -> e.get("hospitalName").toString()));
        map.forEach((k,slist)->{
            Map<String,Object> nmap=new LinkedHashMap<>();
            nmap.put("date",slist.get(0).get("date"));
            nmap.put("hospitalName", slist.get(0).get("hospitalName"));
            //对应医院对应科室和人数
            List<Map<String, Object>> message = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : slist) {
                //数据
                Map<String, Object> messageData = new LinkedHashMap<>();
                messageData.put("department", stringObjectMap.get("department"));
                messageData.put("num", stringObjectMap.get("num"));
                message.add(messageData);
            }
            nmap.put("message",message);
            res.add(nmap);
        });
        allData.put("realTimeInf",res);
        JSONObject json = new JSONObject(allData);
        return json;
    }
}
