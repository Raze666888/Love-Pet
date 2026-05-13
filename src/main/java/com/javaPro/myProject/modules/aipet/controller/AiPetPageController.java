package com.javaPro.myProject.modules.aipet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AI宠物健康咨询 - 页面路由
 * 将URL映射到Thymeleaf模板
 */
@Controller
@RequestMapping("/web")
public class AiPetPageController {

    /**
     * AI宠物健康咨询页面
     * 用户在主页点击"询问AI"后跳转到此页面
     * URL: /web/aipet
     * 模板: templates/webSite/aipet.html
     */
    @GetMapping("/aipet")
    public String aipetPage() {
        return "webSite/aipet";
    }
}
