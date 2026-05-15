package com.javaPro.myProject.modules.aipet.service;

import com.javaPro.myProject.modules.aipet.model.TavilyResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Pet Health AI Consultation Service
 * DeepSeek + Tavily 双AI协作
 * Tavily 负责搜索实时信息，DeepSeek 负责生成专业回答
 */
@Service
public class AiPetService {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.api.model:deepseek-chat}")
    private String model;

    @Value("${openai.proxy.host:}")
    private String proxyHost;

    @Value("${openai.proxy.port:0}")
    private int proxyPort;

    @Autowired
    private TavilySearchService tavilySearchService;

    private RestTemplate restTemplate;

    /**
     * Initialize RestTemplate with proxy support
     */
    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
                SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                factory.setProxy(proxy);
                restTemplate = new RestTemplate(factory);
            } else {
                restTemplate = new RestTemplate();
            }
        }
        return restTemplate;
    }

    /**
     * System prompt - Defines AI's role and capabilities
     */
    private static final String SYSTEM_PROMPT = buildSystemPrompt();

    private static String buildSystemPrompt() {
        return "You are a professional pet health consultant with extensive veterinary knowledge and pet care experience.\n\n"
            + "## Your Responsibilities:\n"
            + "1. Answer pet health questions (diet, behavior, common diseases, vaccines, deworming, etc.)\n"
            + "2. Provide scientific and practical pet care advice\n"
            + "3. Identify situations requiring emergency veterinary care and alert users\n\n"
            + "## Response Guidelines:\n"
            + "- Respond in clear, easy-to-understand English\n"
            + "- Keep answers concise, within 300 words\n"
            + "- For complex questions, use bullet points\n"
            + "- **Important**: If symptoms are severe or urgent, must advise user to seek immediate veterinary care\n"
            + "- Do not provide specific medication dosages or prescriptions (this should be decided by a veterinarian)\n"
            + "- You can suggest users take their pets to a veterinarian for further examination\n\n"
            + "## Common Consultation Areas:\n"
            + "- Diet & Nutrition: Dog/cat food selection, feeding amounts, forbidden foods\n"
            + "- Behavioral Issues: House soiling, destructive behavior, anxiety, aggression\n"
            + "- Vaccination & Deworming: Vaccination schedule, deworming frequency, side effects\n"
            + "- Common Symptoms: Vomiting, diarrhea, coughing, skin problems, loss of appetite\n"
            + "- Breed Characteristics: Common health issues for different breeds\n\n"
            + "## Emergency Situations (Require Immediate Veterinary Care):\n"
            + "- Difficulty breathing, unconsciousness, seizures\n"
            + "- Severe vomiting/diarrhea with blood\n"
            + "- Ingestion of toxic substances\n"
            + "- Severe trauma or fractures\n"
            + "- Not eating or drinking for extended periods\n\n"
            + "Current time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Pet health consultation with Tavily + DeepSeek collaboration
     * 双AI协作：Tavily搜索实时信息，DeepSeek生成专业回答
     *
     * @param question           User's question
     * @param conversationHistory Conversation history (optional)
     * @return AI response
     */
    public String chat(String question, List<Map<String, String>> conversationHistory) {
        // Step 1: Tavily 搜索获取实时信息
        TavilyResult searchResult = tavilySearchService.search(question);

        // Step 2: 构建增强提示词（加入搜索结果）
        String enhancedPrompt = buildEnhancedPrompt(question, searchResult);

        // Step 3: DeepSeek 基于搜索结果生成回答
        return callDeepSeek(enhancedPrompt, conversationHistory);
    }

    /**
     * 构建带搜索结果的增强提示词
     */
    private String buildEnhancedPrompt(String question, TavilyResult searchResult) {
        StringBuilder prompt = new StringBuilder();

        // 添加搜索结果部分
        prompt.append("【参考信息】\n");

        if (searchResult.getAnswer() != null && !searchResult.getAnswer().isEmpty()) {
            prompt.append("概述：").append(searchResult.getAnswer()).append("\n\n");
        }

        // 添加详细搜索结果
        if (searchResult.getResults() != null && !searchResult.getResults().isEmpty()) {
            prompt.append("详细信息：\n");
            for (int i = 0; i < Math.min(3, searchResult.getResults().size()); i++) {
                Map<String, Object> result = searchResult.getResults().get(i);
                String title = (String) result.getOrDefault("title", "N/A");
                String content = (String) result.getOrDefault("content", "");
                // 截取前200字符避免过长
                if (content.length() > 200) {
                    content = content.substring(0, 200) + "...";
                }
                prompt.append(String.format("%d. %s\n   %s\n\n", i + 1, title, content));
            }
        }

        // 添加用户问题
        prompt.append("【用户问题】\n").append(question);

        // 添加回答要求
        prompt.append("\n\n【回答要求】\n");
        prompt.append("请基于以上【参考信息】回答用户问题。如果参考信息不足以回答，请结合你的专业知识回答，");
        prompt.append("并说明信息来源。回答要专业、简洁、实用。");

        return prompt.toString();
    }

    /**
     * 调用 DeepSeek API
     */
    private String callDeepSeek(String prompt, List<Map<String, String>> conversationHistory) {
        // Build message list
        List<Map<String, String>> messages = new ArrayList<>();

        // System prompt
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        // Conversation history
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            messages.addAll(conversationHistory);
        }

        // Current question (enhanced with search results)
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.add(userMsg);

        // Call DeepSeek API
        return callOpenAiApi(messages);
    }

    /**
     * Analyze pet symptoms with Tavily + DeepSeek
     */
    public String analyzeSymptoms(String symptoms, String petType, String petAge) {
        // Tavily 搜索症状相关信息
        String searchQuery = String.format("%s %s symptoms treatment", petType, symptoms);
        TavilyResult searchResult = tavilySearchService.search(searchQuery);

        // 构建症状分析提示词
        StringBuilder prompt = new StringBuilder();
        prompt.append("【参考信息】\n");
        if (searchResult.getAnswer() != null) {
            prompt.append(searchResult.getAnswer()).append("\n\n");
        }

        prompt.append("【宠物信息】\n");
        prompt.append("类型：").append(petType).append("\n");
        prompt.append("年龄：").append(petAge).append("\n");
        prompt.append("症状：").append(symptoms).append("\n\n");

        prompt.append("请基于以上信息，提供：\n");
        prompt.append("1. 可能的原因（列出2-3种最常见情况）\n");
        prompt.append("2. 建议的处理措施\n");
        prompt.append("3. 是否需要立即就医的评估\n");
        prompt.append("4. 预防建议\n\n");
        prompt.append("注意：这只是初步分析，不能替代专业兽医诊断。");

        return callDeepSeek(prompt.toString(), null);
    }

    /**
     * Call OpenAI/DeepSeek Chat Completion API
     */
    private String callOpenAiApi(List<Map<String, String>> messages) {
        try {
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);

            // Set request headers
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + apiKey);

            org.springframework.http.HttpEntity<Map<String, Object>> entity =
                new org.springframework.http.HttpEntity<>(requestBody, headers);

            // Send request
            org.springframework.http.ResponseEntity<Map> response =
                getRestTemplate().exchange(apiUrl, org.springframework.http.HttpMethod.POST, entity, Map.class);

            // Parse response
            if (response.getBody() != null) {
                Map body = response.getBody();
                List choices = (List) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map firstChoice = (Map) choices.get(0);
                    Map message = (Map) firstChoice.get("message");
                    return (String) message.get("content");
                }
            }

            return "AI service is temporarily unavailable. Please try again later.";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI service call failed: " + e.getMessage();
        }
    }
}
