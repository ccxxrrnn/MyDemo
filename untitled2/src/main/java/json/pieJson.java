package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JsonUtil;

import java.util.*;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 9:42
 */

public class pieJson {
    public static void main(String[] args) {
        Map<String,Object> PieChart = new LinkedHashMap<>();
        PieChart.put("chartTitle","渠道结构");
        PieChart.put("unit","万元");
        PieChart.put("chartType","本月累计");
        Map<String,String> dataMap1 = new LinkedHashMap <>();
        dataMap1.put("name","电子渠道");
        dataMap1.put("value","104074.54");
        dataMap1.put("showValue","104,074.54");
        Map<String,String> dataMap2 = new HashMap<>();
        dataMap2.put("name","集团渠道");
        dataMap2.put("value","172322.28");
        dataMap2.put("showValue","172,322.28");
        Map<String,String> dataMap3 = new HashMap<>();
        dataMap3.put("name","实体渠道");
        dataMap3.put("value","761857.98");
        dataMap3.put("showValue","761,857.98");
        Map<String,String> dataMap4 = new HashMap<>();
        dataMap4.put("name","其他渠道");
        dataMap4.put("value","9316.99");
        dataMap4.put("showValue","9,316.99");
        List<Map<String,String>> dataList = new ArrayList<>();
        dataList.add(dataMap1);
        dataList.add(dataMap2);
        dataList.add(dataMap3);
        PieChart.put("data",dataList);
        JsonUtil.show(PieChart);
    }
}
