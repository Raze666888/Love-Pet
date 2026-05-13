package com.example.integration;

import com.javaPro.myProject.SchedulingApplication;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 登录功能集成测试
 * 使用真实的数据库和完整的Spring上下文
 */
@SpringBootTest(classes = SchedulingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("登录功能集成测试")
class LoginIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SysuserService sysuserService;

    private MockMvc mockMvc;
    private MockHttpSession session;
    private Sysuser testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();

        // 创建测试用户数据
        testUser = new Sysuser();
        testUser.setAccount("integrationtest");
        testUser.setPassword("test123");
        testUser.setUsername("集成测试用户");
        testUser.setRole("2");
        testUser.setPhonenumber("13900139000");
        testUser.setSex("女");

        // 插入测试数据到数据库
        sysuserService.insert(testUser);
    }

    /**
     * 测试用例ID: TC_LI_001
     * 测试描述: 完整登录流程集成测试
     */
    @Test
    @DisplayName("TC_LI_001 - 完整登录流程集成测试")
    void testCompleteLoginFlow() throws Exception {
        // 1. 访问登录页面
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        // 2. 执行登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "test123")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.role").value("2"));

        // 3. 验证Session中的用户信息
        mockMvc.perform(get("/admin/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("redirect:/login"));
    }

    /**
     * 测试用例ID: TC_LI_002
     * 测试描述: 数据库用户验证集成测试
     */
    @Test
    @DisplayName("TC_LI_002 - 数据库用户验证集成测试")
    void testDatabaseUserValidation() throws Exception {
        // 使用真实数据库中的用户进行登录测试
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "test123")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.role").value("2"));
    }

    /**
     * 测试用例ID: TC_LI_003
     * 测试描述: 错误凭据集成测试
     */
    @Test
    @DisplayName("TC_LI_003 - 错误凭据集成测试")
    void testWrongCredentialsIntegration() throws Exception {
        // 测试错误密码
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "wrongpassword")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("密码错误"));

        // 测试不存在的用户
        mockMvc.perform(post("/toLogin")
                        .param("account", "nonexistuser")
                        .param("password", "test123")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("登录用户不存在"));
    }

    /**
     * 测试用例ID: TC_LI_004
     * 测试描述: Session管理集成测试
     */
    @Test
    @DisplayName("TC_LI_004 - Session管理集成测试")
    void testSessionManagementIntegration() throws Exception {
        // 1. 登录成功
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "test123")
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 2. 验证Session中存储了用户信息
        // 这里可以添加需要登录才能访问的接口测试

        // 3. 注销登录
        mockMvc.perform(get("/admin/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("redirect:/login"));
    }

    /**
     * 测试用例ID: TC_LI_005
     * 测试描述: 不同角色用户登录集成测试
     */
    @Test
    @DisplayName("TC_LI_005 - 不同角色用户登录集成测试")
    void testDifferentRoleLoginIntegration() throws Exception {
        // 创建管理员用户
        Sysuser adminUser = new Sysuser();
        adminUser.setAccount("admintest");
        adminUser.setPassword("admin123");
        adminUser.setUsername("管理员测试");
        adminUser.setRole("1");
        adminUser.setPhonenumber("13800138001");
        sysuserService.insert(adminUser);

        // 创建服务商用户
        Sysuser providerUser = new Sysuser();
        providerUser.setAccount("providertest");
        providerUser.setPassword("provider123");
        providerUser.setUsername("服务商测试");
        providerUser.setRole("3");
        providerUser.setPhonenumber("13800138002");
        sysuserService.insert(providerUser);

        // 测试管理员登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "admintest")
                        .param("password", "admin123")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.role").value("1"));

        // 测试服务商登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "providertest")
                        .param("password", "provider123")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.role").value("3"));

        // 测试普通用户登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "test123")
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.role").value("2"));
    }

    /**
     * 测试用例ID: TC_LI_006
     * 测试描述: 并发登录集成测试
     */
    @Test
    @DisplayName("TC_LI_006 - 并发登录集成测试")
    void testConcurrentLoginIntegration() throws Exception {
        // 模拟多个用户同时登录
        MockHttpSession session1 = new MockHttpSession();
        MockHttpSession session2 = new MockHttpSession();

        // 创建第二个测试用户
        Sysuser testUser2 = new Sysuser();
        testUser2.setAccount("concurrenttest");
        testUser2.setPassword("test456");
        testUser2.setUsername("并发测试用户");
        testUser2.setRole("2");
        testUser2.setPhonenumber("13900139001");
        sysuserService.insert(testUser2);

        // 用户1登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "integrationtest")
                        .param("password", "test123")
                        .session(session1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 用户2登录
        mockMvc.perform(post("/toLogin")
                        .param("account", "concurrenttest")
                        .param("password", "test456")
                        .session(session2)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证两个用户都能正常注销
        mockMvc.perform(get("/admin/logout")
                        .session(session1))
                .andExpect(status().isOk());

        mockMvc.perform(get("/admin/logout")
                        .session(session2))
                .andExpect(status().isOk());
    }
}
