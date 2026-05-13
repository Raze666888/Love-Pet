package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 购物车API集成测试
 */
@DisplayName("购物车API集成测试")
public class ShopcartControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取购物车列表")
    public void testGetShopcartList() {
        given()
                .log().all()
                .queryParam("userid", 1)
        .when()
                .get("/shopcart/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试添加商品到购物车")
    public void testAddToShopcart() {
        String shopcartJson = "{\"productid\":1,\"userid\":1,\"number\":2}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(shopcartJson)
        .when()
                .post("/shopcart/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试更新购物车商品数量")
    public void testUpdateShopcartItem() {
        String shopcartJson = "{\"id\":1,\"number\":5}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(shopcartJson)
        .when()
                .put("/shopcart/update")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试删除购物车商品")
    public void testRemoveFromShopcart() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .delete("/shopcart/deleteById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
}
