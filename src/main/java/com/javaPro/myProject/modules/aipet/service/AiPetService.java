package com.javaPro.myProject.modules.aipet.service;

import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Calls OpenAI API to provide pet health consultation for users
 */
@Service
public class AiPetService {

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.api.model:gpt-4.1-mini}")
    private String model;

    @Value("${openai.proxy.host:}")
    private String proxyHost;

    @Value("${openai.proxy.port:0}")
    private int proxyPort;

    private RestTemplate restTemplate;

    /**
     * Initialize RestTemplate with proxy support
     */
    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
                // Use proxy
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
     * Pet health consultation (main method)
     *
     * @param question           User's question
     * @param conversationHistory Conversation history (optional)
     * @return AI response
     */
    public String chat(String question, List<Map<String, String>> conversationHistory) {
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

        // Current question
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);

        // Call OpenAI API
        return callOpenAiApi(messages);
    }

    /**
     * Analyze pet symptoms
     */
    public String analyzeSymptoms(String symptoms, String petType, String petAge) {
        String prompt = String.format(
            "Please analyze the following pet symptoms:\n\nPet Type: %s\nPet Age: %s\nSymptom Description: %s\n\n"
            + "Please provide:\n1. Possible causes (list 2-3 most common situations)\n"
            + "2. Recommended course of action\n"
            + "3. Assessment of whether immediate veterinary care is needed\n"
            + "4. Prevention recommendations\n\n"
            + "Note: This is only a preliminary analysis and cannot replace professional veterinary diagnosis.",
            petType, petAge, symptoms
        );
        return chat(prompt, null);
    }

    /**
     * Call OpenAI Chat Completion API
     */
    private String callOpenAiApi(List<Map<String, String>> messages) {
        try {
            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 800);

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
