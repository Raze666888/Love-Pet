package com.javaPro.myProject.modules.provider.controller;

import com.javaPro.myProject.common.controller.BaseController;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.modules.provider.service.ProviderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 服务商控制层
 *
 * @author AjaxResult
 * @since 2024-09-26
 */
@RestController
@RequestMapping("provider")
public class ProviderController extends BaseController {
    
    @Resource
    private ProviderService providerService;

    /**
     * 获取服务商仪表板数据
     *
     * @param providerId 服务商ID
     * @return 仪表板数据
     */
    @GetMapping("/dashboard")
    public AjaxResult getDashboardData(@RequestParam Integer providerId) {
        try {
            Map<String, Object> dashboardData = providerService.getDashboardData(providerId);
            return AjaxResult.ok(dashboardData);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("获取仪表板数据失败: " + e.getMessage());
        }
    }
}
