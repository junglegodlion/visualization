package com.jungle.hotmap.mapper;

import com.jungle.hotmap.bean.ResultBean;
import com.jungle.hotmap.domain.Stat;
import com.alibaba.fastjson.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.MyMapper;

import java.util.List;
import java.util.Map;

@Repository
public interface StatMapper extends MyMapper<Stat> {
    public List<Stat> selectTest();
    public int queryByunitclass();
    public List<JSONObject> selectRight();
    public List<JSONObject> selectMercury();

    /**
     * 统计日期
     * @return
     */
    public List<JSONObject> selectTimes();

    /**
     * 获取不同日期，不同物质的浓度
     * @param fields 物质
     * @param times 日期
     * @return
     */
    public List<Map<String,Object>> selectPollution(@Param("fields") String fields, @Param("times") String times);

    /**
     * 统计有哪些地区
     * @return
     */
    public List<JSONObject> selectName();

    /**
     * 获取某个地区某种物质的一段时间的变化
     * @param substance 物质
     * @param startime 起始时间
     * @param stoptime 终止时间
     * @param site 地区
     * @return
     */
    public List<Map<String,Object>> selectLine(@Param("substance") String substance,@Param("startime") String startime,@Param("stoptime") String stoptime, @Param("site") String site);

}