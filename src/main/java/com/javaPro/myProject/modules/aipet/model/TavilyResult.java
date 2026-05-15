package com.javaPro.myProject.modules.aipet.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * Tavily 搜索结果模型
 */
@Data
public class TavilyResult {
    
    /** Tavily 生成的简要答案 */
    private String answer;
    
    /** 详细搜索结果列表 */
    private List<Map<String, Object>> results;
    
    /** 搜索查询 */
    private String query;
    
    /** 响应时间 */
    private Double responseTime;
}
