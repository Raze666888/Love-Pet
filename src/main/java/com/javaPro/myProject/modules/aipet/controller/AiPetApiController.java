package com.javaPro.myProject.modules.aipet.controller;

import com.javaPro.myProject.modules.aipet.model.ChatRequest;
import com.javaPro.myProject.modules.aipet.model.ChatResponse;
import com.javaPro.myProject.modules.aipet.model.TavilyResult;
import com.javaPro.myProject.modules.aipet.service.AiPetService;
import com.javaPro.myProject.modules.aipet.service.TavilySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AI Pet Health Consultation - API Controller
 * Provides REST API for frontend AJAX calls
 */
@Controller
@RequestMapping("/aipet")
public class AiPetApiController {

    @Autowired
    private AiPetService aiPetService;

    @Autowired
    private TavilySearchService tavilySearchService;

    /**
     * AI Chat API (with Tavily search + DeepSeek generation)
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
                history = new ArrayList<>();
                for (com.javaPro.myProject.modules.aipet.model.ChatMessage msg : request.getConversationHistory()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("role", msg.getRole());
                    map.put("content", msg.getContent());
                    history.add(map);
                }
            }

            // Step 1: Tavily search
            TavilyResult searchResult = tavilySearchService.search(request.getQuestion());

            // Step 2: Extract sources from search results
            List<Map<String, String>> sources = extractSources(searchResult);
            boolean searchUsed = (searchResult.getResults() != null && !searchResult.getResults().isEmpty());

            // Step 3: DeepSeek generates answer (internally uses Tavily results)
            String answer = aiPetService.chat(request.getQuestion(), history);

            // Step 4: Return answer with sources
            return ChatResponse.successWithSources(answer, searchUsed, sources);

        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.fail(e.getMessage());
        }
    }

    /**
     * Symptom Analysis API
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

            // Tavily search
            TavilyResult searchResult = tavilySearchService.search(
                petType + " " + symptoms + " treatment"
            );
            List<Map<String, String>> sources = extractSources(searchResult);
            boolean searchUsed = (searchResult.getResults() != null && !searchResult.getResults().isEmpty());

            String answer = aiPetService.analyzeSymptoms(symptoms, petType, petAge);
            return ChatResponse.successWithSources(answer, searchUsed, sources);

        } catch (Exception e) {
            e.printStackTrace();
            return ChatResponse.fail(e.getMessage());
        }
    }

    /**
     * Extract source information from Tavily results
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, String>> extractSources(TavilyResult searchResult) {
        List<Map<String, String>> sources = new ArrayList<>();

        if (searchResult == null || searchResult.getResults() == null) {
            return sources;
        }

        for (Map<String, Object> result : searchResult.getResults()) {
            Map<String, String> source = new HashMap<>();
            source.put("title", (String) result.getOrDefault("title", ""));
            source.put("url", (String) result.getOrDefault("url", ""));
            // Extract domain name from URL
            String url = (String) result.getOrDefault("url", "");
            try {
                String domain = url.replaceFirst("^https?://", "").split("/")[0];
                source.put("domain", domain);
            } catch (Exception e) {
                source.put("domain", url);
            }
            sources.add(source);
        }

        return sources;
    }
}
