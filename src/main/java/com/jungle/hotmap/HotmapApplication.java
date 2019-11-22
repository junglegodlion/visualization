package com.jungle.hotmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.jungle.hotmap.mapper")
public class HotmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotmapApplication.class, args);
    }

}
