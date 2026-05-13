package com.javaPro.myProject.modules.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaPro.myProject.modules.product.dto.ServiceProviderFilterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 产品控制器测试类
 * 测试服务筛选功能
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 测试基本的产品查询功能
     */
    @Test
    public void testQueryByPage() throws Exception {
        mockMvc.perform(get("/product")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试时间筛选功能
     */
    @Test
    public void testFilterByTime() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setServiceStartTime("09:00");
        filterDTO.setServiceEndTime("18:00");
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试价格区间筛选功能
     */
    @Test
    public void testFilterByPriceRange() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setMinPrice(50.0);
        filterDTO.setMaxPrice(200.0);
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试评分筛选功能
     */
    @Test
    public void testFilterByRating() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setMinRating(4.0);
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试复合条件筛选功能
     */
    @Test
    public void testFilterByMultipleConditions() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setServiceStartTime("09:00");
        filterDTO.setServiceEndTime("18:00");
        filterDTO.setMinPrice(50.0);
        filterDTO.setMaxPrice(300.0);
        filterDTO.setMinRating(3.5);
        filterDTO.setKeyword("宠物");
        filterDTO.setSortBy("price_asc");
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试GET方式的筛选接口
     */
    @Test
    public void testFilterServicesGet() throws Exception {
        mockMvc.perform(get("/product/filterServices")
                .param("serviceStartTime", "09:00")
                .param("serviceEndTime", "18:00")
                .param("minPrice", "50")
                .param("maxPrice", "200")
                .param("minRating", "4.0")
                .param("keyword", "宠物")
                .param("sortBy", "price_asc")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试空筛选条件
     */
    @Test
    public void testFilterWithEmptyConditions() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试无效的价格区间
     */
    @Test
    public void testFilterWithInvalidPriceRange() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setMinPrice(500.0);  // 最低价格大于最高价格
        filterDTO.setMaxPrice(100.0);
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(0)); // 应该没有结果
    }

    /**
     * 测试服务时间段更新功能
     */
    @Test
    public void testUpdateServiceTime() throws Exception {
        mockMvc.perform(post("/product/updateServiceTime"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试按服务类型筛选
     */
    @Test
    public void testFilterByServiceType() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setProductType(1); // 假设1是某个服务类型ID
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    /**
     * 测试排序功能
     */
    @Test
    public void testSortByPrice() throws Exception {
        ServiceProviderFilterDTO filterDTO = new ServiceProviderFilterDTO();
        filterDTO.setSortBy("price_asc");
        filterDTO.setPageNum(1);
        filterDTO.setPageSize(10);

        mockMvc.perform(post("/product/filterServices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
