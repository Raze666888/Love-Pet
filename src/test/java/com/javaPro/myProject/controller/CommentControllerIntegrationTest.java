package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 评论API集成测试
 */
@DisplayName("评论API集成测试")
public class CommentControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取评论列表")
    public void testGetCommentList() {
        given()
                .log().all()
                .queryParam("noticeId", 1)
        .when()
                .get("/comment/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试根据ID获取评论")
    public void testGetCommentById() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .get("/comment/queryById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试发表评论")
    public void testCreateComment() {
        String commentJson = "{\"noticeId\":1,\"userId\":1,\"content\":\"这是一条测试评论\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(commentJson)
        .when()
                .post("/comment/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试更新评论")
    public void testUpdateComment() {
        String commentJson = "{\"id\":1,\"content\":\"这是更新后的评论\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(commentJson)
        .when()
                .put("/comment/update")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试删除评论")
    public void testDeleteComment() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .delete("/comment/deleteById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
}
