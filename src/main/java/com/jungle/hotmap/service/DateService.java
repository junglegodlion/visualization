package com.jungle.hotmap.service;

import java.util.List;

/**
 * 接口类
 * 实现时间与时间戳转换
 */
public interface DateService {
    /**
     * 批处理
     * 时间戳转时间
     * @return
     */
    public List<String> timeStamp2Date();

    /**
     * 单个
     * 时间转时间戳
     * @return
     */
    public String Date2timeStamp(String dates);

    /**
     * 单个
     * 时间戳转时间
     * @return
     */
    public String singleTimeStamp2Date(String dates);


}
