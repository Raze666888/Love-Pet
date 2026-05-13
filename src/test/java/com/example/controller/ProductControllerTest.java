package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商品控制器测试类
 * 覆盖测试规格说明书中的商品管理功能测试
 * 
 * 修复：调整断言以匹配控制器返回的 AjaxResult/ListByPage 结构
 */
@SpringBootTest(classes = com.javaPro.myProject.SchedulingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setProductname("宠物美容服务");
        testProduct.setProductdes("专业宠物美容护理");
        testProduct.setKedanjia("100.00");
        testProduct.setKucun("10");
        testProduct.setChandi("北京市朝阳区");
        testProduct.setCompanyid(1);

        Product product2 = new Product();
        product2.setId(2);
        product2.setProductname("宠物寄养服务");
        product2.setProductdes("安全可靠的宠物寄养");
        product2.setKedanjia("80.00");
        product2.setKucun("5");
        product2.setChandi("北京市海淀区");
        product2.setCompanyid(1);

        productList = Arrays.asList(testProduct, product2);
    }

    /**
     * 测试用例ID: TC_PC_001
     * 测试描述: 分页查询商品列表成功
     * 对应测试规格说明书: 4.1.1 商品管理控制器
     */
    @Test
    void testQueryByPage_Success() throws Exception {
        when(productService.queryByPage(any(Product.class))).thenReturn(productList);

        mockMvc.perform(get("/product")
                        .param("productname", "宠物")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].productname").value("宠物美容服务"))
                .andExpect(jsonPath("$.list[1].productname").value("宠物寄养服务"));
    }

    /**
     * 测试用例ID: TC_PC_002
     * 测试描述: 根据ID查询商品详情成功
     * 对应角色功能测试规格说明书: 3.1 商品浏览功能
     */
    @Test
    void testQueryById_Success() throws Exception {
        when(productService.queryById(anyInt(), any())).thenReturn(testProduct);

        mockMvc.perform(get("/product/detail")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.productname").value("宠物美容服务"))
                .andExpect(jsonPath("$.data.kedanjia").value("100.00"));
    }

    /**
     * 测试用例ID: TC_PC_003
     * 测试描述: 添加新商品成功
     * 对应角色功能测试规格说明书: 4.1.1 商品基础管理 TC_MP_001
     */
    @Test
    void testAdd_Success() throws Exception {
        when(productService.insert(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productname").value("宠物美容服务"));
    }

    /**
     * 测试用例ID: TC_PC_004
     * 测试描述: 更新商品信息成功
     * 对应角色功能测试规格说明书: 4.1.1 商品基础管理 TC_MP_002
     */
    @Test
    void testEdit_Success() throws Exception {
        testProduct.setProductname("更新后的宠物美容服务");
        when(productService.update(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(put("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.productname").value("更新后的宠物美容服务"));
    }

    /**
     * 测试用例ID: TC_PC_005
     * 测试描述: 删除商品成功
     * 对应角色功能测试规格说明书: 4.1.1 商品基础管理 TC_MP_004
     */
    @Test
    void testDelete_Success() throws Exception {
        when(productService.deleteById(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/product")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试用例ID: TC_PC_006
     * 测试描述: 查询不存在的商品
     * 边界条件测试
     */
    @Test
    void testQueryById_NotFound() throws Exception {
        when(productService.queryById(anyInt(), any())).thenReturn(null);

        mockMvc.perform(get("/product/detail")
                        .param("id", "999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * 测试用例ID: TC_PC_007
     * 测试描述: 商品查询异常处理
     * 异常情况测试
     */
    @Test
    void testQueryByPage_Exception() throws Exception {
        when(productService.queryByPage(any(Product.class)))
                .thenThrow(new RuntimeException("数据库连接异常"));

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("查询失败: 数据库连接异常"));
    }

    /**
     * 测试用例ID: TC_PC_008
     * 测试描述: 商品库存检查
     * 对应角色功能测试规格说明书: 4.1.2 库存管理 TC_IM_001
     */
    @Test
    void testStockCheck() throws Exception {
        testProduct.setKucun("0"); // 库存为0
        when(productService.queryById(anyInt(), any())).thenReturn(testProduct);

        mockMvc.perform(get("/product/detail")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.kucun").value("0"));
    }

    /**
     * 测试用例ID: TC_PC_009
     * 测试描述: 空列表查询
     * 边界条件测试
     */
    @Test
    void testQueryByPage_EmptyList() throws Exception {
        when(productService.queryByPage(any(Product.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isEmpty())
                .andExpect(jsonPath("$.total").value(0));
    }
}
