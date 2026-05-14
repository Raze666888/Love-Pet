package com.javaPro.myProject.modules.aipet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AI Pet Health Consultation - Page Controller
 * Maps URLs to Thymeleaf templates
 */
@Controller
@RequestMapping("/web")
public class AiPetPageController {

    /**
     * AI Pet Health Consultation Page
     * Users are redirected to this page after clicking "Ask AI" on the homepage
     * URL: /web/aipet
     * Template: templates/webSite/aipet.html
     */
    @GetMapping("/aipet")
    public String aipetPage() {
        return "webSite/aipet";
    }
}
