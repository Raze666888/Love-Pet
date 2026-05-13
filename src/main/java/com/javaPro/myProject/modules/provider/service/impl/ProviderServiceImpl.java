package com.javaPro.myProject.modules.provider.service.impl;

import com.javaPro.myProject.modules.provider.service.ProviderService;
import com.javaPro.myProject.modules.order.dao.OrderDao;
import com.javaPro.myProject.modules.orderEvalute.dao.OrderEvaluteDao;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.producttype.dao.ProducttypeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 服务商服务实现类
 *
 * @author AjaxResult
 * @since 2024-09-26
 */
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private OrderDao orderDao;
    
    @Resource
    private OrderEvaluteDao orderEvaluteDao;
    
    @Resource
    private ProductDao productDao;
    
    @Resource
    private ProducttypeDao producttypeDao;

    @Override
    public Map<String, Object> getDashboardData(Integer providerId) {
        System.out.println("获取服务商仪表板数据，providerId: " + providerId);

        Map<String, Object> result = new HashMap<>();

        try {
            // 基础统计数据
            Map<String, Object> dashboardData = new HashMap<>();

            // 总订单数
            Long totalOrders = orderDao.countByCompanyId(providerId);
            dashboardData.put("totalOrders", totalOrders != null ? totalOrders : 0L);
            System.out.println("总订单数: " + totalOrders);

            // 总收入
            Double totalRevenue = orderDao.getTotalRevenueByCompanyId(providerId);
            dashboardData.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
            System.out.println("总收入: " + totalRevenue);

            // 平均评分
            Double avgRating = orderEvaluteDao.getAvgRatingByCompanyId(providerId);
            dashboardData.put("avgRating", avgRating != null ? avgRating : 0.0);
            System.out.println("平均评分: " + avgRating);

            // 服务数量
            Long totalServices = productDao.countByCompanyId(providerId);
            dashboardData.put("totalServices", totalServices != null ? totalServices : 0L);
            System.out.println("服务数量: " + totalServices);

            result.put("Data", dashboardData);

            // 服务类型分布数据
            List<Map<String, Object>> serviceTypeData = getServiceTypeData(providerId);
            result.put("serviceTypeData", serviceTypeData);
            System.out.println("服务类型数据条数: " + serviceTypeData.size());

            // 评分分布数据
            List<Integer> ratingData = getRatingDistributionData(providerId);
            result.put("ratingData", ratingData);
            System.out.println("评分分布数据: " + ratingData);

            // 订单趋势数据（最近30天）
            List<Map<String, Object>> orderTrendData = getOrderTrendData(providerId);
            result.put("orderTrendData", orderTrendData);
            System.out.println("订单趋势数据条数: " + orderTrendData.size());

            // 最近订单
            List<Map<String, Object>> recentOrders = getRecentOrders(providerId);
            result.put("recentOrders", recentOrders);
            System.out.println("最近订单数据条数: " + recentOrders.size());

            System.out.println("仪表板数据获取成功");
            return result;

        } catch (Exception e) {
            System.err.println("获取仪表板数据时发生错误: " + e.getMessage());
            e.printStackTrace();

            // 返回默认数据
            Map<String, Object> defaultData = new HashMap<>();
            defaultData.put("totalOrders", 0L);
            defaultData.put("totalRevenue", 0.0);
            defaultData.put("avgRating", 0.0);
            defaultData.put("totalServices", 0L);

            result.put("Data", defaultData);
            result.put("serviceTypeData", new ArrayList<>());
            result.put("ratingData", Arrays.asList(0, 0, 0, 0, 0));
            result.put("orderTrendData", new ArrayList<>());
            result.put("recentOrders", new ArrayList<>());

            return result;
        }
    }
    
    /**
     * 获取服务类型分布数据
     */
    private List<Map<String, Object>> getServiceTypeData(Integer providerId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询该服务商的所有产品类型统计
        List<Map<String, Object>> typeStats = productDao.getProductTypeStatsByCompanyId(providerId);
        
        for (Map<String, Object> stat : typeStats) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", stat.get("type_name"));
            item.put("value", stat.get("count"));
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * 获取评分分布数据
     */
    private List<Integer> getRatingDistributionData(Integer providerId) {
        List<Integer> result = Arrays.asList(0, 0, 0, 0, 0); // 1-5星的数量
        
        List<Map<String, Object>> ratingStats = orderEvaluteDao.getRatingDistributionByCompanyId(providerId);
        
        for (Map<String, Object> stat : ratingStats) {
            Integer rating = (Integer) stat.get("rating");
            Long count = (Long) stat.get("count");
            if (rating >= 1 && rating <= 5) {
                result.set(rating - 1, count.intValue());
            }
        }
        
        return result;
    }
    
    /**
     * 获取订单趋势数据（最近30天）
     */
    private List<Map<String, Object>> getOrderTrendData(Integer providerId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 生成最近30天的日期
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        
        for (int i = 29; i >= 0; i--) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            
            String date = sdf.format(calendar.getTime());
            Long count = orderDao.getOrderCountByDateAndCompanyId(calendar.getTime(), providerId);
            
            Map<String, Object> item = new HashMap<>();
            item.put("date", date);
            item.put("count", count != null ? count : 0);
            result.add(item);
        }
        
        return result;
    }
    
    /**
     * 获取最近订单
     */
    private List<Map<String, Object>> getRecentOrders(Integer providerId) {
        return orderDao.getRecentOrdersByCompanyId(providerId, 10);
    }
}
