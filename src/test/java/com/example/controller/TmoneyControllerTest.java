package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaPro.myProject.modules.tmoney.controller.TmoneyController;
import com.javaPro.myProject.modules.tmoney.entity.Tmoney;
import com.javaPro.myProject.modules.tmoney.service.TmoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 充值控制器测试类
 * 覆盖角色功能测试规格说明书中的充值管理功能测试
 * 
 * 修复：调整断言以匹配控制器返回的 AjaxResult/ListByPage 结构
 */
@SpringBootTest(classes = com.javaPro.myProject.SchedulingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TmoneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TmoneyService tmoneyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Tmoney testTmoney;
    private List<Tmoney> tmoneyList;

    @BeforeEach
    void setUp() {
        testTmoney = new Tmoney();
        testTmoney.setId(1);
        testTmoney.setUserid(1);
        testTmoney.setMoney("100.00");
        testTmoney.setCreatetime(new Date());
        testTmoney.setAuditstatus("已审核"); // 充值成功

        Tmoney tmoney2 = new Tmoney();
        tmoney2.setId(2);
        tmoney2.setUserid(1);
        tmoney2.setMoney("50.00");
        tmoney2.setCreatetime(new Date());
        tmoney2.setAuditstatus("待审核"); // 待处理

        tmoneyList = Arrays.asList(testTmoney, tmoney2);
    }

    /**
     * 测试用例ID: TC_TC_001
     * 测试描述: 分页查询充值记录成功
     * 对应角色功能测试规格说明书: 3.3.1 充值功能
     */
    @Test
    void testQueryByPage_Success() throws Exception {
        when(tmoneyService.queryByPage(any(Tmoney.class))).thenReturn(tmoneyList);

        mockMvc.perform(get("/tmoney")
                        .param("userid", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].money").value("100.00"))
                .andExpect(jsonPath("$.list[1].money").value("50.00"));
    }

    /**
     * 测试用例ID: TC_TC_002
     * 测试描述: 根据ID查询充值记录成功
     * 对应角色功能测试规格说明书: 3.3.1 充值功能
     */
    @Test
    void testQueryById_Success() throws Exception {
        when(tmoneyService.queryById(1)).thenReturn(testTmoney);

        mockMvc.perform(get("/tmoney/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.money").value("100.00"))
                .andExpect(jsonPath("$.data.auditstatus").value("已审核"));
    }

    /**
     * 测试用例ID: TC_TC_003
     * 测试描述: 用户充值成功
     * 对应角色功能测试规格说明书: 3.3.1 充值功能 TC_RF_001
     */
    @Test
    void testAdd_Success() throws Exception {
        when(tmoneyService.insert(any(Tmoney.class))).thenReturn(testTmoney);

        mockMvc.perform(post("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTmoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.money").value("100.00"))
                .andExpect(jsonPath("$.data.userid").value(1));
    }

    /**
     * 测试用例ID: TC_TC_004
     * 测试描述: 更新充值记录状态
     * 管理员功能测试
     */
    @Test
    void testEdit_Success() throws Exception {
        testTmoney.setAuditstatus("已审核"); // 审核通过
        when(tmoneyService.update(any(Tmoney.class))).thenReturn(testTmoney);

        mockMvc.perform(put("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTmoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.auditstatus").value("已审核"));
    }

    /**
     * 测试用例ID: TC_TC_005
     * 测试描述: 删除充值记录
     * 管理员功能测试
     */
    @Test
    void testDelete_Success() throws Exception {
        when(tmoneyService.deleteById(1)).thenReturn(true);

        mockMvc.perform(delete("/tmoney")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试用例ID: TC_TC_006
     * 测试描述: 充值金额验证
     * 边界条件测试
     */
    @Test
    void testRechargeAmountValidation() throws Exception {
        // 测试最小充值金额
        Tmoney minTmoney = new Tmoney();
        minTmoney.setUserid(1);
        minTmoney.setMoney("1.00"); // 最小金额
        when(tmoneyService.insert(any(Tmoney.class))).thenReturn(minTmoney);

        mockMvc.perform(post("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minTmoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.money").value("1.00"));

        // 测试大额充值
        Tmoney maxTmoney = new Tmoney();
        maxTmoney.setUserid(1);
        maxTmoney.setMoney("10000.00"); // 大额充值
        when(tmoneyService.insert(any(Tmoney.class))).thenReturn(maxTmoney);

        mockMvc.perform(post("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(maxTmoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.money").value("10000.00"));
    }

    /**
     * 测试用例ID: TC_TC_007
     * 测试描述: 充值状态管理
     * 对应角色功能测试规格说明书: 3.3.1 充值功能
     */
    @Test
    void testRechargeStatusManagement() throws Exception {
        // 测试待处理状态
        Tmoney pendingTmoney = new Tmoney();
        pendingTmoney.setId(1);
        pendingTmoney.setAuditstatus("待审核"); // 待处理
        when(tmoneyService.queryById(1)).thenReturn(pendingTmoney);

        mockMvc.perform(get("/tmoney/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.auditstatus").value("待审核"));

        // 测试处理成功状态
        pendingTmoney.setAuditstatus("已审核"); // 处理成功
        when(tmoneyService.update(any(Tmoney.class))).thenReturn(pendingTmoney);

        mockMvc.perform(put("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pendingTmoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.auditstatus").value("已审核"));
    }

    /**
     * 测试用例ID: TC_TC_008
     * 测试描述: 用户充值历史查询
     * 对应角色功能测试规格说明书: 3.3.1 充值功能
     */
    @Test
    void testUserRechargeHistory() throws Exception {
        // 查询特定用户的充值记录
        when(tmoneyService.queryByPage(any(Tmoney.class))).thenReturn(tmoneyList);

        mockMvc.perform(get("/tmoney")
                        .param("userid", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list.length()").value(2));
    }

    /**
     * 测试用例ID: TC_TC_009
     * 测试描述: 查询不存在的充值记录
     * 边界条件测试
     */
    @Test
    void testQueryById_NotFound() throws Exception {
        when(tmoneyService.queryById(999)).thenReturn(null);

        mockMvc.perform(get("/tmoney/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    /**
     * 测试用例ID: TC_TC_010
     * 测试描述: 充值记录统计
     * 对应角色功能测试规格说明书: 5.5.1 财务统计
     */
    @Test
    void testRechargeStatistics() throws Exception {
        // 查询所有充值记录用于统计
        when(tmoneyService.queryByPage(any(Tmoney.class))).thenReturn(tmoneyList);

        mockMvc.perform(get("/tmoney")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].money").value("100.00"))
                .andExpect(jsonPath("$.list[1].money").value("50.00"));
    }
}
