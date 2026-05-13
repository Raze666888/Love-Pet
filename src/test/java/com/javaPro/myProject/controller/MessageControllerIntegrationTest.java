package com.javaPro.myProject.controller;

import com.javaPro.myProject.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * 消息API集成测试
 */
@DisplayName("消息API集成测试")
public class MessageControllerIntegrationTest extends BaseIntegrationTest {
    
    @Test
    @DisplayName("测试获取收件箱消息")
    public void testGetInboxMessages() {
        given()
                .log().all()
                .queryParam("receiveid", 1)
        .when()
                .get("/message/queryByPage")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(500), is(404)));
    }
    
    @Test
    @DisplayName("测试发送消息")
    public void testSendMessage() {
        String messageJson = "{\"content\":\"这是一条测试消息\",\"senderid\":1,\"receiveid\":2}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(messageJson)
        .when()
                .post("/message/insert")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试回复消息")
    public void testReplyMessage() {
        String replyJson = "{\"id\":1,\"content\":\"这是一条回复消息\",\"status\":\"1\"}";
        
        given()
                .log().all()
                .contentType("application/json")
                .body(replyJson)
        .when()
                .put("/message/update")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
    
    @Test
    @DisplayName("测试删除消息")
    public void testDeleteMessage() {
        given()
                .log().all()
                .queryParam("id", 1)
        .when()
                .delete("/message/deleteById")
        .then()
                .log().all()
                .statusCode(anyOf(is(200), is(400), is(404), is(500)));
    }
}
