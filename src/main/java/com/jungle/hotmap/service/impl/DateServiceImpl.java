package com.jungle.hotmap.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jungle.hotmap.mapper.StatMapper;
import com.jungle.hotmap.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DateServiceImpl implements DateService {
    /**
     * 注入 DAO
     */
    @Autowired
    private StatMapper statMapper;

    /**
     * 时间戳转时间
     * @return
     */
    @Override
    public List<String> timeStamp2Date() {
        List<Object> objects = new ArrayList<>();
        List<String> objects2 = new ArrayList<>();

        List<JSONObject> jsonObjects = statMapper.selectTimes();
        jsonObjects.forEach(jsonObject ->
                objects.add(jsonObject.get("timestamp"))
        );
        System.out.println(objects);
        for (Object object : objects) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(object))));      // 时间戳转换成时间
            objects2.add(sd+"");

        }
        return objects2;
    }

    /**
     * 时间转时间戳
     * @param dates
     * @return
     */
    @Override
    public String Date2timeStamp(String dates) {


//        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        String time=dates;
        Date date = null;
        try {
            date = format.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("格式化结果:"+date.getTime());
        long time1 = date.getTime();
        System.out.println(time1);

        return time1+"";
    }

    /**
     * 单个
     * 时间戳转时间
     * @return
     */
    @Override
    public String singleTimeStamp2Date(String dates) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(dates))));
        return sd;
    }

}
