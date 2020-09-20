package com.small.small;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 * <p>
 * Create By wesson 2020-09-17 22:44
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonTest {
    @Test
    public void replaceJson() {
        String url = "http://baidu.com";

        String jsonStr = "{\"errno\":0,\"data\":{\"modelDescription\":\"极效前置all_in_one\",\"modelName\":\"极效前置\",\"sePlat\":\"fdc-planning\",\"modelId\":4,\"modelStruct\":{\"b12d3df\":{\"upstream\":[\"d0115de\"],\"name\":\"输出数据\",\"downstream\":[],\"id\":\"b12d3df\",\"label\":\"zipout\",\"info\":{}},\"17db456\":{\"upstream\":[],\"downstream\":[\"d0115de\"],\"id\":\"17db456\",\"label\":\"zipin\",\"info\":{\"fileName\":\"input.zip\",\"fileUrl\":\"http://sssssss\"}},\"d0115de\":{\"upstream\":[\"17db456\"],\"name\":\"模型\",\"downstream\":[\"b12d3df\"],\"id\":\"d0115de\",\"label\":\"fdcPython\",\"info\":{\"-p\":\"0\",\"-r\":\"0\",\"-d\":\"${PATH}\",\"-t\":\"0\",\"-f\":\"0\",\"-l\":\"${PATH}\"}}},\"modelType\":0},\"errmsg\":\"success\",\"logId\":\"686322387490366649\"}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        System.out.println(jsonObject);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject modelStruct = data.getJSONObject("modelStruct");
        JSONObject object = modelStruct.getJSONObject("17db456");
        JSONObject info = object.getJSONObject("info");
        //替换url
        info.put("fileUrl", url);
        JSONObject d0115de = modelStruct.getJSONObject("d0115de");
        JSONObject info2 = d0115de.getJSONObject("info");
        //替换计算类型 0=候选 1=提交 2=重算
        info2.put("-t", 1);


        System.out.println(jsonObject);


    }
}
