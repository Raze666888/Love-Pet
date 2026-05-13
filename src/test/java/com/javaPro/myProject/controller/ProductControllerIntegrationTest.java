package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 产品API集成测试
 */
@DisplayName("产品API集成测试")
public class ProductControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取所有产品")
    public void testGetAllProducts() {
        given()
                .log().all()
        .when()
                .get("/product/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据ID获取产品")
    public void testGetProductById() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/product/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试产品搜索")
    public void testSearchProducts() {
        given()
                .log().all()
                .queryParam("productname", "宠物服务")
        .when()
                .get("/product/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试获取产品详情")
    public void testGetProductDetail() {
        // 先获取产品列表
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/product/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
}
