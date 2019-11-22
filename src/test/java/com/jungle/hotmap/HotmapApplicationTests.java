package com.jungle.hotmap;

import com.alibaba.fastjson.JSONObject;
import com.jungle.hotmap.bean.ResultBean;
import com.jungle.hotmap.mapper.StatMapper;
import com.jungle.hotmap.service.DateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.jungle.hotmap.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class HotmapApplicationTests {

    /**
     * 注入 DAO
     */
    @Autowired
    private StatMapper statMapper;


    @Autowired
    private DateService dateService;

    @Test
    public void contextLoads() {
    }



    @Test
    public void testCount() {
        int i = statMapper.queryByunitclass();
        System.out.println(i);
    }
    @Test
    public void testRight() {
        List<JSONObject> resultBeans = statMapper.selectRight();
        resultBeans.forEach(resultBean -> {
            System.out.println(resultBean);
        });
    }

    @Test
    public void testTimes() {
        ArrayList<Object> objects = new ArrayList<>();

        List<JSONObject> jsonObjects = statMapper.selectTimes();
        jsonObjects.forEach(jsonObject ->
                objects.add(jsonObject.get("timestamp"))
                );
        System.out.println(objects);
//        System.out.println(jsonObjects.get(0).get("timestamp").getClass());
//
//        Date date = new Date((Long)jsonObjects.get(0).get("timestamp"));
//        System.out.println(date);
//        Long timeStamp = (Long)jsonObjects.get(0).get("timestamp");  //获取当前时间戳
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));      // 时间戳转换成时间
//        System.out.println("格式化结果：" + sd);

        for (Object object : objects) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(object))));      // 时间戳转换成时间
            System.out.println("格式化结果：" + sd);

        }
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        String time="2019-10-21";
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("格式化结果:"+date.getTime());
        long time1 = date.getTime();
        System.out.println(time1);

    }
    @Test
    public void testPollution() {

        List<Map<String, Object>> maps  = statMapper.selectPollution("plumbum","1571587200000");
        System.out.println(maps);
    }

    @Test
    public void testLie(){
        List<Map<String, Object>> maps = statMapper.selectLine("mercury", "1571587200000", "1572364800000", "'山东省淄博'");
        for (Map<String, Object> stringObjectMap : maps) {
            Object times = stringObjectMap.get("times");
            String s = dateService.singleTimeStamp2Date(times + "");

            System.out.println(times);
            stringObjectMap.put("times",s);
        }
        System.out.println(maps);
//        String pro_siteid="'"+"山东省淄博"+"'";
//        System.out.println(pro_siteid);
    }

}
