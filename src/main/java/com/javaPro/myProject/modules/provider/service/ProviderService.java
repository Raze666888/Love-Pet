package com.javaPro.myProject.modules.provider.service;

import java.util.Map;

/**
 * 服务商服务接口
 *
 * @author AjaxResult
 * @since 2024-09-26
 */
public interface ProviderService {
    
    /**
     * 获取服务商仪表板数据
     *
     * @param providerId 服务商ID
     * @return 仪表板数据
     */
    Map<String, Object> getDashboardData(Integer providerId);
}
