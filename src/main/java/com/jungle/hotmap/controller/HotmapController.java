package com.jungle.hotmap.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jungle.hotmap.service.DateService;
import com.jungle.hotmap.service.HotmapService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HotmapController {

    @Autowired
    private HotmapService hotmapService;

    @Autowired
    private DateService dateService;




    @RequestMapping(value = "/hotmap", method = RequestMethod.GET)
    public String index() {

        List<JSONObject> jsonObjects = hotmapService.selectMercury();
////        JSONArray jsonArray = JSONArray.fromObject(results);
//
        System.out.println("是否有数据");
        System.out.println(jsonObjects);
//
//        model.addAttribute("data", jsonObjects);

        return "hotmap";
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ResponseBody
   public List<String> getLoction(){
       List<String> list = dateService.timeStamp2Date();
        System.out.println(list);
       return list;
   }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public String getData(String sid,String pollutionid) {

        System.out.println("sid is " + sid);
        System.out.println("pollutionid" + pollutionid);

        Map<String, String> map = new HashMap<String, String>();
        map.put("汞", "mercury");
        map.put("铅", "plumbum");
        map.put("石油", "petroleum");
        System.out.println(map.get(pollutionid));
        String str=map.get(pollutionid);

        String s = dateService.Date2timeStamp(sid);
        System.out.println(s);
        List<Map<String, Object>> maps = hotmapService.selectPollution(str,s);
//        JSONArray jsonArray = JSONArray.fromObject(results);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(maps);
        String jsonStr = JSON.toJSONString( maps );
        System.out.println(jsonStr);
        return jsonStr;


    }
}
