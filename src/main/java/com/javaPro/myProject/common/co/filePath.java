package com.javaPro.myProject.common.co;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**

 * @create-date: 2023/6/12 9:45
 */
@Configuration
public class filePath {
    @Bean
    public String getPath(){
        return System.getProperty("user.dir") + "/src/main/resources/";
    }
}
