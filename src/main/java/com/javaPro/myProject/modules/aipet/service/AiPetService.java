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
 * 宠物健康AI咨询服务
 * 调用OpenAI API，为用户提供宠物健康咨询
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
     * 初始化 RestTemplate，支持代理
     */
    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
                // 使用代理
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
     * 系统提示词 - 定义AI的角色和能力
     */
    private static final String SYSTEM_PROMPT = buildSystemPrompt();

    private static String buildSystemPrompt() {
        return "你是一位专业的宠物健康顾问，拥有丰富的兽医知识和宠物护理经验。\n\n"
            + "## 你的职责：\n"
            + "1. 解答宠物健康问题（饮食、行为、常见疾病、疫苗、驱虫等）\n"
            + "2. 提供科学、实用的宠物护理建议\n"
            + "3. 识别需要紧急就医的情况并提醒用户\n\n"
            + "## 回答原则：\n"
            + "- 用通俗易懂的中文回答\n"
            + "- 回答要简洁明了，控制在300字以内\n"
            + "- 对于复杂问题，分点说明\n"
            + "- **重要提醒**：如果症状严重或紧急，必须建议用户立即就医\n"
            + "- 不要给出具体的药物剂量或处方（这应由兽医决定）\n"
            + "- 可以建议用户带宠物去兽医处做进一步检查\n\n"
            + "## 常见咨询领域：\n"
            + "- 饮食营养：狗粮/猫粮选择、喂食量、禁忌食物\n"
            + "- 行为问题：乱拉乱尿、破坏行为、焦虑、攻击性\n"
            + "- 疫苗驱虫：接种时间、驱虫频率、副作用\n"
            + "- 常见症状：呕吐、腹泻、咳嗽、皮肤病、食欲不振\n"
            + "- 品种特性：不同品种的常见健康问题\n\n"
            + "## 紧急情况识别（需立即就医）：\n"
            + "- 呼吸困难、昏迷、抽搐\n"
            + "- 严重呕吐/腹泻伴随血便\n"
            + "- 误食有毒物质\n"
            + "- 严重外伤或骨折\n"
            + "- 长时间不进食不喝水\n\n"
            + "当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * 宠物健康咨询（主方法）
     *
     * @param question           用户问题
     * @param conversationHistory 对话历史（可选）
     * @return AI回答
     */
    public String chat(String question, List<Map<String, String>> conversationHistory) {
        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();

        // 系统提示词
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.add(systemMsg);

        // 历史对话
        if (conversationHistory != null && !conversationHistory.isEmpty()) {
            messages.addAll(conversationHistory);
        }

        // 当前问题
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);

        // 调用OpenAI API
        return callOpenAiApi(messages);
    }

    /**
     * 分析宠物症状
     */
    public String analyzeSymptoms(String symptoms, String petType, String petAge) {
        String prompt = String.format(
            "请分析以下宠物症状：\n\n宠物类型：%s\n宠物年龄：%s\n症状描述：%s\n\n"
            + "请提供：\n1. 可能的原因（列举2-3种最常见的情况）\n"
            + "2. 建议的处理方式\n"
            + "3. 是否需要立即就医的判断\n"
            + "4. 预防措施建议\n\n"
            + "注意：这只是初步分析，不能替代专业兽医诊断。",
            petType, petAge, symptoms
        );
        return chat(prompt, null);
    }

    /**
     * 调用OpenAI Chat Completion API
     */
    private String callOpenAiApi(List<Map<String, String>> messages) {
        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 800);

            // 设置请求头
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + apiKey);

            org.springframework.http.HttpEntity<Map<String, Object>> entity =
                new org.springframework.http.HttpEntity<>(requestBody, headers);

            // 发送请求
            org.springframework.http.ResponseEntity<Map> response =
                getRestTemplate().exchange(apiUrl, org.springframework.http.HttpMethod.POST, entity, Map.class);

            // 解析响应
            if (response.getBody() != null) {
                Map body = response.getBody();
                List choices = (List) body.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map firstChoice = (Map) choices.get(0);
                    Map message = (Map) firstChoice.get("message");
                    return (String) message.get("content");
                }
            }

            return "AI服务暂时无响应，请稍后再试。";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI服务调用失败：" + e.getMessage();
        }
    }
}
