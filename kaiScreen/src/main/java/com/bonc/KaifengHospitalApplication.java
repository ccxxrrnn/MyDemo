package com.bonc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Author xiongwei
 * @WriteTime 2021-03-24 11:37
 */

@SpringBootApplication
public class KaifengHospitalApplication extends SpringBootServletInitializer  {
    public static void main(String[] args) {
        SpringApplication.run(KaifengHospitalApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
            return builder.sources(KaifengHospitalApplication.class);
    }
}
