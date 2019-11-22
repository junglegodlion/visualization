mysql -uroot -p -h192.168.1.18 -P9906

## 1.docker 安装MySQL



--docker-compose.yml

```yaml
version: "2"
services:
        db:
                image: "mysql:5.7"
                environment:
                        MYSQL_ROOT_PASSWORD: "yourpasswd"
                        MYSQL_USER: 'root'
                        MYSQL_PASS: 'yourpasswd'
                restart: always
                volumes:
                        - "/home/stan/docker_app/mysql/data:/var/lib/mysql"
                        - "/home/stan/docker_app/mysql/config/my.cnf:/etc/my.cnf"
                ports:
                        - "3306:3306"
```

--my.cnf

```
[mysqld]
port=3306
character_set_server=utf8mb4
default-storage-engine=INNODB
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4
[client]
default-character-set=utf8mb4

```

## 2.建立数据库

```sql
create database soil charset=utf8;
```

## 3.建表

```sql
create table pollution(
id int auto_increment primary key comment "主键",
name varchar(30) not null comment "名字",
times bigint(20) not null comment "时间戳",
longitude double not null comment "经度",
latitude double not null comment "纬度",
mercury double not null comment "汞",
plumbum double not null comment "铅",
petroleum double not null comment "石油"
);
```

## 4.造数据

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import random
import pandas as pd
import numpy as np
from geopy.geocoders import Nominatim
import time
import requests
import pymysql

"""
组合得出省市名称
"""
def get_address(x):
    address = []
    s = list(x.keys())  # 省列表
    for i in s:
        sheng = i
        for j in x[sheng]:
            city = j
            name = str(sheng) + str(city)
            address.append(name)
    return address

"""
生成经纬度
"""
# 方法一，这个不是很好用
# geolocator = Nominatim()
# def get_latitude_longitude(name,num):
#     latitude_longitude = None
#     for i in range(10):
#         try:
#             location = geolocator.geocode(name, timeout=200)
#             latitude_longitude = str(location.latitude) + "," + str(location.longitude)
#             print(name + '第' + str(i) +'次' + ' 砍杀')
#             return latitude_longitude.split(",")[num]
#         except Exception as e:
#             print(e)
#             time.sleep(1)
#             print(name+ ' 本次超时，等1秒继续')
#
#     if latitude_longitude == None:
#         return 0

# 方法二：应用高德api
def get_latitude_longitude(address):
    par = {'address': address, 'key': 'b95db2c663cf3e34ee731f70e1469bf1'}
    base = 'http://restapi.amap.com/v3/geocode/geo'
    response = requests.get(base, par)
    answer = response.json()
    # print(answer)
    GPS = answer['geocodes'][0]['location'].split(",")
    return GPS[0], GPS[1]


"""
随机生成0-1之间的小数
"""
def generate_random(num):
    list=[]
    for i in range(0,num):
        m=('%.3f' % random.random())
        list.append(m)
    return list


"""
生成日期列表
转成字符串
"""
def generate_date(date,num):
    date_list=[]
    date = pd.date_range(date, periods=num)
    ts_list = [str(ele) for ele in date]
    print(ts_list)
    for i in ts_list:
        # print(i)
        # 转换成时间数组
        timeArray = time.strptime(i, "%Y-%m-%d %H:%M:%S")
        # print(timeArray)
        # 转换成时间戳
        timestamp = time.mktime(timeArray)
        timestamp2=str(timestamp).split(".")[0]+"000"
        date_list.append(timestamp2)
    return date_list

"""
将数据整理成可存入MySQL的样式
"""
def mysql_data(x,date,num):
    list=[]
    address=get_address(x)
    for i in address:
        latitude,longitude=get_latitude_longitude(i)
        timestamp=generate_date(date,num)
        for j in timestamp:
            data=[]
            data.append("0")
            num_random=generate_random(3)
            data.append(i)
            data.append(j)
            data.append(latitude)
            data.append(longitude)


            for m in num_random:
                data.append(m)
            print(data)
            list.append(data)
    return list



"""
保存列表至文件
"""
def save_txt(file_name,list):
    with open(file_name, 'w') as f:
        f.write(list)



"""
将数据存入数据库
"""

def save_mysql(list):
    print("进来了")
    try:
        # 1.链接 数据库  链接对象 connection()
        conn = pymysql.Connect(
            host="192.168.1.18",
            port=9906,
            db='soil',
            user='root',
            passwd="123456",
            charset='utf8'
        )
        # 2. 创建 游标对象 cursor()
        cur = conn.cursor()
        for i in list:
            print(i)
            sql = "insert into pollution(name,times,latitude,longitude,mercury,plumbum,petroleum) values('" + i[1] + "','" + i[2] + "','" + i[3]+ "','" + i[4] +"','"+ i[5] + "','" + i[6]+ "','" + i[7] +"')"
            # sql = "insert into goods(title,link,comment) values('" + i[1] + "','" + i[2] + "','" + i[3] + "')"
            print(sql)
            result = cur.execute(sql)
        # 提交事务
        conn.commit()
        # 关闭游标
        cur.close()
        # 关闭链接
        conn.close()

    except Exception as e:
        print(e)



if __name__ == '__main__':
    x = {'河北省': ['石家庄', '唐山', '秦皇岛', '承德'],
         '山东省': ['济南', '青岛', '临沂', '淄博'],
         '湖南省': ['长沙', '衡阳', '湘潭', '邵阳', '岳阳', '株洲'],
         '江西省': ['南昌', '九江', '上饶', '景德镇']}
    # address = get_address(x)
    # print(address)
    # date=generate_date("20181010",5)
    # print(date)
    # m=generate_random(4)
    # print(m)

    # 获取想要的数据格式
    list=mysql_data(x,"20191021",10)
    print(list)
    print("开始向MySQL保存数据")
    # 存入MySQL数据库
    save_mysql(list)
    # str_list=str(list)
    # save_txt("list.txt",str_list)

```

## 5.测试

```sql
select name,latitude,longitude,mercury from pollution where times=1571587200000;
```

```sql
select distinct times from pollution;
```

```sql
select mercury from pollution where times between 1571587200000 and 1572364800000 and name='山东省淄博';
```

```
select distinct name from pollution;
```

```
select distinct name,mercury from pollution where times=1571587200000;
```



## 6.前端页面

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring4-4.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>热力图</title>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style>
        html,
        body,
        #container {
            margin: 0;
            padding: 0;
            width: 100%;
            height: 100%;
        }


        .location{
            font-size: 20px;
            font-family: Consolas;

        }
    </style>
</head>
<body>


<div style="text-align: center;">
    <span class="location"> 热力图 </span> <br>
    <div style="display: inline-block">
<!--        <span class="location"> 地点: </span>-->
        <select  id="sid" οnchange="selectcity()">
            <option>---请选择日期---</option>
        </select>
    </div>
    <div style="display: inline-block">
        <select  id="pollutionid" οnchange="selectcity()">
            <option>---请选择物质---</option>
                    <option>铅</option>
                    <option>石油</option>
                    <option>汞</option>
        </select>
    </div>
    <div style="display: inline-block">
        <button type="button" id="btn_submit" onclick="btn()"> 查询 </button>
    </div>

</div>
<!-- <h1>整合高德地图完成实时热力图展示</h1>-->
<!--<h1 style="text-align: center">热力图</h1>-->
<!--<div style="text-align: center">-->
<!--    <h1 style="display: inline;">刷新频率：</h1>-->
<!--    <div style="display: inline-block">-->
<!--        <select class="sel-btn" style="display: inline-block">-->
<!--            <option>30秒</option>-->
<!--            <option>60秒</option>-->
<!--            <option>90秒</option>-->
<!--        </select>-->
<!--    </div>-->


<!--</div>-->


<div id="container">
</div>
<div class="input-card" style="width: auto;">
    <div class="input-item">
        <button class="btn" onclick="heatmap.show()">显示热力图</button>
    </div>
    <div class="input-item">
        <button class="btn" onclick="heatmap.hide()">关闭热力图</button>
<!--        <span class="test">csnb</span>-->
    </div>
</div>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="//webapi.amap.com/maps?v=1.4.15&key=b95db2c663cf3e34ee731f70e1469bf1"></script>
<script src="//a.amap.com/jsapi_demos/static/resource/heatmapData.js"></script>

<!--    1、js的 <script type="text/javascript"> 替换成 <script th:inline="javascript">-->

<script th:inline="javascript">

    $(document).ready(function () {
        $.get('/map/location',function (result) {
            console.log(result);
            for (var i = 0;i < result.length; ++i) {
                $("#sid").prepend("<option value="+ result[i] +">"+ result[i] +"</option>");
            }

        })
    });

    var res_data;

    console.log("res_data is" + res_data);
    function btn(){
        var result1=$("#sid").val();
        var result2=$("#pollutionid").val();
        $.ajax({
            type: "POST",
            url: "/map/data",
            dataType: 'json',
            data:{
                sid:result1,
                pollutionid:result2
            },
            async: false,
            success: function(result) {
                res_data= result;
                // console.log("this is ");
                console.log(result);
                console.log("res is " + res_data)
                heatmap.setDataSet({
                    //lng为经度值，lat为纬度值，count为权重
                    data:res_data,
                    //权重最大值
                    max: 1
                });
            }
        })
    }



    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [106.55,29.57],
        zoom: 7
    });

    map.setFeatures(['road','point'])//多个种类要素显示
    //测量距离
    map.plugin(["AMap.MouseTool"],function(){
        var mousetool = new AMap.MouseTool(map);
        mousetool.rule(); //使用鼠标工具，在地图上画标记点
        mousetool.measureArea(); //使用鼠标工具，测量面积
    });

    if (!isSupportCanvas()) {
        alert('热力图仅对支持canvas的浏览器适用,您所使用的浏览器不能使用热力图功能,请换个浏览器试试~')
    }

    //详细的参数,可以查看heatmap.js的文档 http://www.patrick-wied.at/static/heatmapjs/docs.html
    //参数说明如下:
    /* visible 热力图是否显示,默认为true
     * opacity 热力图的透明度,分别对应heatmap.js的minOpacity和maxOpacity
     * radius 势力图的每个点的半径大小, 网格热力半径，单位：米
     * gradient  {JSON} 热力图的渐变区间,热力图的透明度 . gradient如下所示
     *	{
     .2:'rgb(0, 255, 255)',
     .5:'rgb(0, 110, 255)',
     .8:'rgb(100, 0, 255)'
     }
     其中 key 表示插值的位置, 0-1
     value 为颜色值
     *
     热力聚合模式，count 为点数量加和
        mode: 'count',
     */

    // 坐标点
    //后端通过Thymeleaf向前端传入数据
    // var points = [[${data}]];
    // console.log("ps is" + JSON.stringify(points));

    var heatmap;
    map.plugin(["AMap.Heatmap"], function () {
        //初始化heatmap对象

        heatmap = new AMap.Heatmap(map, {

            radius: 25, //给定半径
            opacity: [0, 0.8]
            ,
            gradient:{
                0.5: 'blue',
                0.65: 'rgb(117,211,248)',
                0.7: 'rgb(0, 255, 0)',
                0.9: '#ffea00',
                1.0: 'red'
            }
        });
        // 设置数据集：该数据为北京部分“公园”数据
        heatmap.setDataSet({
            //lng为经度值，lat为纬度值，count为权重
            data:res_data,
            //权重最大值
            max: 1
        });

        //工具条
        AMap.plugin(['AMap.ToolBar'],function(){//异步同时加载多个插件
            var toolbar = new AMap.ToolBar();
            map.addControl(toolbar);

        });
    });

    //判断浏览区是否支持canvas
    function isSupportCanvas() {
        var elem = document.createElement('canvas');
        return !!(elem.getContext && elem.getContext('2d'));
    }
</script>
</body>
</html>
```

