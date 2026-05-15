package com.javaPro.myProject.modules.aipet.service;

import com.javaPro.myProject.modules.aipet.model.TavilyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Tavily 搜索服务
 * 负责搜索实时宠物健康信息
 */
@Service
public class TavilySearchService {

    private static final Logger log = LoggerFactory.getLogger(TavilySearchService.class);

    @Value("${tavily.api.key:}")
    private String apiKey;

    @Value("${tavily.search.depth:advanced}")
    private String searchDepth;

    @Value("${tavily.max.results:5}")
    private int maxResults;

    private static final String TAVILY_API_URL = "https://api.tavily.com/search";

    private RestTemplate restTemplate;

    /**
     * 获取配置了超时的 RestTemplate
     */
    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(10000);  // 连接超时 10秒
            factory.setReadTimeout(15000);     // 读取超时 15秒
            restTemplate = new RestTemplate(factory);
        }
        return restTemplate;
    }

    /**
     * 搜索宠物健康相关信息
     *
     * @param query 用户查询
     * @return Tavily搜索结果
     */
    public TavilyResult search(String query) {
        try {
            log.info("[Tavily] Searching for: {}", query);

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("api_key", apiKey);
            requestBody.put("query", query);
            requestBody.put("search_depth", searchDepth);
            requestBody.put("max_results", maxResults);
            requestBody.put("include_answer", true);
            requestBody.put("include_images", false);
            requestBody.put("include_raw_content", false);

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<Map> response = getRestTemplate().postForEntity(
                TAVILY_API_URL,
                entity,
                Map.class
            );

            // 解析响应
            TavilyResult result = parseResponse(response.getBody(), query);

            if (result.getResults() != null && !result.getResults().isEmpty()) {
                log.info("[Tavily] Search successful, found {} results", result.getResults().size());
            } else {
                log.warn("[Tavily] Search returned no results for: {}", query);
            }

            return result;

        } catch (Exception e) {
            log.error("[Tavily] Search failed for query '{}': {}", query, e.getMessage());
            // 返回空结果，让DeepSeek基于自身知识回答
            TavilyResult emptyResult = new TavilyResult();
            emptyResult.setQuery(query);
            emptyResult.setAnswer(null);
            emptyResult.setResults(null);
            return emptyResult;
        }
    }

    /**
     * 解析Tavily响应
     */
    @SuppressWarnings("unchecked")
    private TavilyResult parseResponse(Map<String, Object> body, String query) {
        TavilyResult result = new TavilyResult();
        result.setQuery(query);

        if (body == null) {
            return result;
        }

        // 解析答案
        if (body.containsKey("answer")) {
            result.setAnswer((String) body.get("answer"));
        }

        // 解析结果列表
        if (body.containsKey("results")) {
            result.setResults((java.util.List<Map<String, Object>>) body.get("results"));
        }

        // 解析响应时间
        if (body.containsKey("response_time")) {
            result.setResponseTime(((Number) body.get("response_time")).doubleValue());
        }

        return result;
    }
}
