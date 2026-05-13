package com.javaPro.myProject.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * 接口测试基类
 * 用于Controller层的集成测试
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class BaseIntegrationTest {
    
    @LocalServerPort
    protected int port;
    
    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }
}
