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
 * AI Pet Health Consultation - API Controller
 * Provides REST API for frontend AJAX calls
 */
@Controller
@RequestMapping("/aipet")
public class AiPetApiController {

    @Autowired
    private AiPetService aiPetService;

    /**
     * AI Chat API
     * Frontend calls this endpoint via AJAX POST
     *
     * Request example:
     * POST /aipet/chat
     * {
     *   "question": "What should I do if my dog is vomiting?",
     *   "conversationHistory": [
     *     {"role": "user", "content": "Previous question"},
     *     {"role": "assistant", "content": "Previous answer"}
     *   ]
     * }
     */
    @PostMapping("/chat")
    @ResponseBody
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            if (request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
                return ChatResponse.fail("Question cannot be empty");
            }

            // Convert ChatMessage list to Map list
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
     * Symptom Analysis API
     *
     * Request example:
     * POST /aipet/analyze
     * {
     *   "symptoms": "vomiting, diarrhea, lethargy",
     *   "petType": "dog",
     *   "petAge": "3 years"
     * }
     */
    @PostMapping("/analyze")
    @ResponseBody
    public ChatResponse analyzeSymptoms(@RequestBody Map<String, String> params) {
        try {
            String symptoms = params.get("symptoms");
            String petType = params.getOrDefault("petType", "unknown");
            String petAge = params.getOrDefault("petAge", "unknown");

            if (symptoms == null || symptoms.trim().isEmpty()) {
                return ChatResponse.fail("Symptom description cannot be empty");
            }

            String answer = aiPetService.analyzeSymptoms(symptoms, petType, petAge);
            return ChatResponse.success(answer);

        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.fail(e.getMessage());
        }
    }
}
