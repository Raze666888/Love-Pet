package com.javaPro.myProject.modules.aipet.controller;

import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.modules.aipet.model.ChatRequest;
import com.javaPro.myProject.modules.aipet.model.ChatResponse;
import com.javaPro.myProject.modules.aipet.service.AiPetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI宠物健康咨询 - API接口
 * 提供REST API供前端AJAX调用
 */
@Controller
@RequestMapping("/aipet")
public class AiPetApiController {

    @Autowired
    private AiPetService aiPetService;

    /**
     * AI聊天接口
     * 前端通过AJAX POST调用此接口
     *
     * 请求示例：
     * POST /aipet/chat
     * {
     *   "question": "狗狗呕吐怎么办？",
     *   "conversationHistory": [
     *     {"role": "user", "content": "之前的问题"},
     *     {"role": "assistant", "content": "之前的回答"}
     *   ]
     * }
     */
    @PostMapping("/chat")
    @ResponseBody
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
                return ChatResponse.fail("问题不能为空");
            }

            // 将ChatMessage列表转换为Map列表
            List<Map<String, String>> history = null;
            if (request.getConversationHistory() != null) {
                history = new java.util.ArrayList<>();
                for (com.javaPro.myProject.modules.aipet.model.ChatMessage msg : request.getConversationHistory()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("role", msg.getRole());
                    map.put("content", msg.getContent());
                    history.add(map);
                }
            }

            String answer = aiPetService.chat(request.getQuestion(), history);
            return ChatResponse.success(answer);

        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.fail(e.getMessage());
        }
    }

    /**
     * 症状分析接口
     *
     * 请求示例：
     * POST /aipet/analyze
     * {
     *   "symptoms": "呕吐、腹泻、精神萎靡",
     *   "petType": "狗",
     *   "petAge": "3岁"
     * }
     */
    @PostMapping("/analyze")
    @ResponseBody
    public ChatResponse analyzeSymptoms(@RequestBody Map<String, String> params) {
        try {
            String symptoms = params.get("symptoms");
            String petType = params.getOrDefault("petType", "未知");
            String petAge = params.getOrDefault("petAge", "未知");

            if (symptoms == null || symptoms.trim().isEmpty()) {
                return ChatResponse.fail("症状描述不能为空");
            }

            String answer = aiPetService.analyzeSymptoms(symptoms, petType, petAge);
            return ChatResponse.success(answer);

        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.fail(e.getMessage());
        }
    }
}
