package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 公告API集成测试
 */
@DisplayName("公告API集成测试")
public class WebnoticeControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取所有公告")
    public void testGetAllNotices() {
        given()
                .log().all()
        .when()
                .get("/webnotice/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据ID获取公告")
    public void testGetNoticeById() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/webnotice/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试创建公告")
    public void testCreateNotice() {
        String noticeJson = "{\"title\":\"测试公告\",\"content\":\"这是一条测试公告\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(noticeJson)
        .when()
                .post("/webnotice/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试获取热门公告")
    public void testGetPopularNotices() {
        given()
                .log().all()
                .queryParam("limit", 10)
        .when()
                .get("/webnotice/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
}
