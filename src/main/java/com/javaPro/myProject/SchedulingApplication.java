package com.javaPro.myProject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.javaPro.myProject.modules.*.dao,com.javaPro.myProject.modules.*.mapper")
public class SchedulingApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SchedulingApplication.class);
        springApplication.setAllowCircularReferences(Boolean.TRUE);
        springApplication.run(args);

    }

}
