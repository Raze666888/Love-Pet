package com.javaPro.myProject.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * 单元测试基类
 * 用于Service层的单元测试
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class BaseUnitTest {
    // 所有单元测试类都可以继承此类
}
