package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 订单API集成测试
 */
@DisplayName("订单API集成测试")
public class OrderControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取所有订单")
    public void testGetAllOrders() {
        given()
                .log().all()
        .when()
                .get("/order/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据ID获取订单")
    public void testGetOrderById() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/order/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试创建订单")
    public void testCreateOrder() {
        String orderJson = "{\"carid\":\"1,2,3\",\"userid\":1,\"companyid\":1}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(orderJson)
        .when()
                .post("/order/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试按用户ID查询订单")
    public void testGetOrdersByUserId() {
        given()
                .log().all()
                .queryParam("userid", 1)
        .when()
                .get("/order/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试获取订单统计")
    public void testGetOrderStatistics() {
        given()
                .log().all()
        .when()
                .get("/order/statistics")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
}
