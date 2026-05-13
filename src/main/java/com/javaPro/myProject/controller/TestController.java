package com.javaPro.myProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试控制器
 * 用于测试静态资源访问
 */
@Controller
@RequestMapping("/test")
public class TestController {
    
    /**
     * 静态资源测试页面
     * @return 测试页面
     */
    @GetMapping("/static")
    public String testStatic() {
        return "test-static";
    }

    /**
     * 服务筛选页面
     * @return 服务筛选页面
     */
    @GetMapping("/service-filter")
    public String serviceFilter() {
        return "service-filter";
    }

    /**
     * 静态资源测试页面 - 简化版
     * @return 简化的静态资源测试页面
     */
    @GetMapping("/static-simple")
    public String staticSimple() {
        return "static-simple";
    }

    /**
     * 评论功能测试页面
     * @return 评论功能测试页面
     */
    @GetMapping("/comment")
    public String testComment() {
        return "test-comment";
    }
}
