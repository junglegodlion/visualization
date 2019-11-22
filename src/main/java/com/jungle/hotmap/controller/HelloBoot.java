package com.jungle.hotmap.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jungle.hotmap.service.DateService;
import com.jungle.hotmap.service.HotmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是我们的第一个Boot应用
 */
@Controller
public class HelloBoot {
    @Autowired
    private HotmapService hotmapService;

    @Autowired
    private DateService dateService;


    @RequestMapping(value = "/linechart", method = RequestMethod.GET)
    public String firstDemo() {
        return "test";
    }
    @RequestMapping(value = "/site", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getLoction(){
        List<String> objects = new ArrayList<>();
        List<JSONObject> jsonObjects = hotmapService.selectName();
        jsonObjects.forEach(jsonObject ->
                objects.add(jsonObject.get("name")+"")
        );
        System.out.println(jsonObjects);
        return objects;
    }

    @RequestMapping(value = "/line", method = RequestMethod.POST)
    @ResponseBody
    public String getLine(String siteid,String startid,String stopid,String pollutionid) {

        //将中文物质换成英文
        Map<String, String> map = new HashMap<String, String>();
        map.put("汞", "mercury");
        map.put("铅", "plumbum");
        map.put("石油", "petroleum");
        System.out.println(map.get(pollutionid));
        String str_pollution=map.get(pollutionid);

        //将时间换成时间戳
        String s1 = dateService.Date2timeStamp(startid);
        String s2 = dateService.Date2timeStamp(stopid);

        //对地点进行处理，加单引号
        String pro_siteid="'"+siteid+"'";


        List<Map<String, Object>> maps = hotmapService.selectLine(str_pollution,s1,s2,pro_siteid);

        //对maps进行处理，使时间戳变为时间的形式
        for (Map<String, Object> stringObjectMap : maps) {
            Object times = stringObjectMap.get("times");
            String s = dateService.singleTimeStamp2Date(times + "");

            System.out.println(times);
            stringObjectMap.put("times",s);
        }



//        JSONArray jsonArray = JSONArray.fromObject(results);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(maps);
        String jsonStr = JSON.toJSONString( maps );
        System.out.println(jsonStr);
        return jsonStr;


    }
}
