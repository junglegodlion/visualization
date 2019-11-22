package com.jungle.hotmap.service.impl;

import com.jungle.hotmap.mapper.StatMapper;
import com.alibaba.fastjson.JSONObject;
import com.jungle.hotmap.service.HotmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HotmapServiceImpl implements HotmapService {

    /**
     * 注入 DAO
     */
    @Autowired
    private StatMapper statMapper;


    @Override
    public List<JSONObject> selectTest() {
        return null;
    }

    @Override
    public List<JSONObject> selectRight() {
        return statMapper.selectRight();
    }

    @Override
    public List<JSONObject> selectMercury() {
        return statMapper.selectMercury();
    }

    @Override
    public List<JSONObject> selectTimes() {
        return statMapper.selectTimes();
    }

    @Override
    public List<Map<String, Object>> selectPollution(String fields, String times) {
        return statMapper.selectPollution(fields,times);
    }

    @Override
    public List<JSONObject> selectName() {
        return statMapper.selectName();
    }

    @Override
    public List<Map<String, Object>> selectLine(String substance, String startime, String stoptime, String site) {
        return statMapper.selectLine(substance,startime,stoptime,site);
    }
}
