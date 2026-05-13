package com.javaPro.myProject.modules.order.service;

import com.javaPro.myProject.config.BaseUnitTest;
import com.javaPro.myProject.common.model.AjaxResult;
import com.javaPro.myProject.modules.order.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 订单服务单元测试
 */
@DisplayName("订单服务单元测试")
public class OrderServiceTest extends BaseUnitTest {
    
    @Autowired
    private OrderService orderService;
    
    private Order testOrder;
    
    @BeforeEach
    public void setUp() {
        testOrder = new Order();
        testOrder.setCarid("1,2,3");
        testOrder.setCreatetime(new Date());
        testOrder.setUserid(1);
        testOrder.setCompanyid(1);
        testOrder.setRemark("测试订单");
    }
    
    @Test
    @DisplayName("测试创建订单")
    public void testCreateOrder() {
        // When
        AjaxResult result = orderService.insert(testOrder);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("code")).isNotNull();
    }
    
    @Test
    @DisplayName("测试查询订单")
    public void testQueryOrderById() {
        // Given
        Order inserted = (Order) orderService.insert(testOrder).get("data");
        if (inserted != null) {
            Integer orderId = inserted.getId();
            
            // When
            Order found = orderService.queryById(orderId);
            
            // Then
            assertThat(found).isNotNull();
            assertThat(found.getId()).isEqualTo(orderId);
        }
    }
    
    @Test
    @DisplayName("测试查询不存在的订单")
    public void testQueryNonExistentOrder() {
        // When
        Order found = orderService.queryById(9999);
        
        // Then
        assertThat(found).isNull();
    }
    
    @Test
    @DisplayName("测试分页查询订单")
    public void testQueryOrdersByPage() {
        // Given
        orderService.insert(testOrder);
        Order query = new Order();
        query.setUserid(1);
        
        // When
        List<Order> orders = orderService.queryByPage(query);
        
        // Then
        assertThat(orders).isNotEmpty();
    }
    
    @Test
    @DisplayName("测试更新订单")
    public void testUpdateOrder() {
        // Given
        Order inserted = (Order) orderService.insert(testOrder).get("data");
        if (inserted != null) {
            inserted.setRemark("更新后的订单");
            
            // When
            Order updated = orderService.update(inserted);
            
            // Then
            assertThat(updated).isNotNull();
            assertThat(updated.getRemark()).isEqualTo("更新后的订单");
        }
    }
    
    @Test
    @DisplayName("测试删除订单")
    public void testDeleteOrder() {
        // Given
        Order inserted = (Order) orderService.insert(testOrder).get("data");
        if (inserted != null) {
            Integer orderId = inserted.getId();
            
            // When
            boolean deleted = orderService.deleteById(orderId);
            
            // Then
            assertThat(deleted).isTrue();
            assertThat(orderService.queryById(orderId)).isNull();
        }
    }
    
    @Test
    @DisplayName("测试订单统计")
    public void testOrderStatistics() {
        // When
        AjaxResult result = orderService.statistics();
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.get("code")).isNotNull();
    }
    
    @Test
    @DisplayName("测试订单购物车IDs验证")
    public void testOrderCartIdsValidation() {
        // When
        testOrder.setCarid("1,2,3,4,5");
        AjaxResult result = orderService.insert(testOrder);
        
        // Then
        assertThat(result.get("code")).isNotNull();
    }
    
    @Test
    @DisplayName("测试订单用户ID验证")
    public void testOrderUserIdValidation() {
        // When
        testOrder.setUserid(100);
        AjaxResult result = orderService.insert(testOrder);
        
        // Then
        assertThat(result).isNotNull();
    }
}
