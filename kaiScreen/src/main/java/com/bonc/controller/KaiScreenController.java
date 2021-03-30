package com.bonc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bonc.service.KaiScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 11:47
 */

@RestController
@Api(value = "KaiScreenController")
@RequestMapping("/kaiScreen")
public class KaiScreenController {

    @Resource
    KaiScreenService kaiScreenService;

    final String HOSPITALIZATION = "1";
    final String OUTPATIENTS = "0";
    final String NOWDATE = "2018032301";

    @PostMapping("/outpatients")
    @ApiOperation(notes = "xw", value = "展示排名相关信息根据进入的typy类型")
    public JSONObject showRankListByType(@RequestParam("type") String type){
        if (HOSPITALIZATION.equals(type)){
            return JSON.parseObject("住院");
        }
        if (OUTPATIENTS.equals(type)){
            JSONObject json = kaiScreenService.showOutpatientsRankListByDate(NOWDATE);
            return json;
        }
        return JSON.parseObject("404");
    }

    @PostMapping("/outpatientsInDetail")
    @ApiOperation(notes = "xw", value = "展示更加详细的信息根据进入的typy类型和医院id")
    public JSONObject showDetailByTypeAndHospitalId(@RequestParam("type") String type,
                                         @RequestParam(value = "hospitalId" ,required = false) String hospitalId){
        if (type.equals(HOSPITALIZATION)){
            return JSONObject.parseObject("住院详细信息");
        }
        if (type.equals(OUTPATIENTS)){
            if (hospitalId == null){
                hospitalId = "000014";
            }
            return kaiScreenService.showOutpatientsInDetailByHospitalIdAndDate(hospitalId, NOWDATE);
        }
        return JSONObject.parseObject("404");
    }

    @PostMapping("/outpatientsPopulation")
    @ApiOperation(notes = "xw", value = "展示实时重点信息")
    public JSONObject showPopulationByType(@RequestParam("type") String type){
        if (type.equals(HOSPITALIZATION)){
            return JSONObject.parseObject("住院详细信息");
        }
        if (type.equals(OUTPATIENTS)){
            return kaiScreenService.showOutpatientsPopulationByDate(NOWDATE);
        }
        return JSONObject.parseObject("404");
    }

}
