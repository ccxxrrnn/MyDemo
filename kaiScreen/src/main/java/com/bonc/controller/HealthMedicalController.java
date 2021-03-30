package com.bonc.controller;

import com.alibaba.fastjson.JSONObject;
import com.bonc.service.HealthCareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-25 11:43
 */

@RestController
@Api(value = "HealthMedicalController")
@RequestMapping("/healthCare")
public class HealthMedicalController {

    @Resource
    HealthCareService healthCareService;

    String NOWDATE = "20180319";

    @PostMapping("/buyMadical")
    @ApiOperation(notes = "xw", value = "显示医保购药统计")
    public JSONObject showMedicalStatistics(){
        JSONObject jsonObject = healthCareService.showMedicalStatisticsByDate(NOWDATE);
        return jsonObject;
    }
}
