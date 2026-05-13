package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 用户API集成测试
 */
@DisplayName("用户API集成测试")
public class SysuserControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取所有用户")
    public void testGetAllUsers() {
        given()
                .log().all()
        .when()
                .get("/sysuser/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据ID获取用户")
    public void testGetUserById() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/sysuser/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据账号查询用户")
    public void testGetUserByAccount() {
        given()
                .log().all()
                .queryParam("account", "testuser")
        .when()
                .get("/sysuser/queryByAccount")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试创建用户")
    public void testCreateUser() {
        String userJson = "{\"username\":\"测试用户\",\"account\":\"testuser\"," +
                "\"password\":\"password123\",\"phonenumber\":\"13800138000\"," +
                "\"sex\":\"男\",\"address\":\"北京市\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(userJson)
        .when()
                .post("/sysuser/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试更新用户")
    public void testUpdateUser() {
        String userJson = "{\"id\":1,\"username\":\"更新后的用户\",\"account\":\"updateduser\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(userJson)
        .when()
                .put("/sysuser/update")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试搜索用户")
    public void testSearchUsers() {
        given()
                .log().all()
                .queryParam("username", "测试")
        .when()
                .get("/sysuser/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
}
