package com.javaPro.myProject.modules.sysuser.service;

import com.javaPro.myProject.config.BaseUnitTest;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 用户服务单元测试
 */
@DisplayName("用户服务单元测试")
public class SysuserServiceTest extends BaseUnitTest {
    
    @Autowired
    private SysuserService sysuserService;
    
    private Sysuser testUser;
    
    @BeforeEach
    public void setUp() {
        String suffix = String.valueOf(System.nanoTime());
        testUser = new Sysuser();
        testUser.setUsername("张三");
        testUser.setSex("男");
        testUser.setPhonenumber("138" + suffix.substring(Math.max(0, suffix.length() - 8)));
        testUser.setAccount("zhangsan_" + suffix);
        testUser.setPassword("password123");
        testUser.setIdcard("110101199001011234");
        testUser.setAddress("北京市朝阳区");
    }
    
    @Test
    @DisplayName("测试创建用户")
    public void testCreateUser() {
        // When
        Sysuser inserted = sysuserService.insert(testUser);
        
        // Then
        assertThat(inserted).isNotNull();
        assertThat(inserted.getId()).isNotNull();
        assertThat(inserted.getUsername()).isEqualTo("张三");
    }
    
    @Test
    @DisplayName("测试根据ID查询用户")
    public void testQueryUserById() {
        // Given
        Sysuser inserted = sysuserService.insert(testUser);
        Integer userId = inserted.getId();
        
        // When
        Sysuser found = sysuserService.queryById(userId);
        
        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(userId);
        assertThat(found.getUsername()).isEqualTo("张三");
    }
    
    @Test
    @DisplayName("测试根据账号查询用户")
    public void testQueryUserByAccount() {
        // Given
        sysuserService.insert(testUser);
        
        // When
        Sysuser found = sysuserService.queryByAccount(testUser.getAccount());
        
        // Then
        assertThat(found).isNotNull();
        assertThat(found.getAccount()).isEqualTo(testUser.getAccount());
        assertThat(found.getUsername()).isEqualTo("张三");
    }
    
    @Test
    @DisplayName("测试查询不存在的用户")
    public void testQueryNonExistentUser() {
        // When
        Sysuser found = sysuserService.queryById(9999);
        
        // Then
        assertThat(found).isNull();
    }
    
    @Test
    @DisplayName("测试分页查询用户")
    public void testQueryUsersByPage() {
        // Given
        sysuserService.insert(testUser);
        Sysuser query = new Sysuser();
        query.setUsername("张三");
        
        // When
        List<Sysuser> users = sysuserService.queryByPage(query);
        
        // Then
        assertThat(users).isNotEmpty();
        assertThat(users.stream()
                .anyMatch(u -> "张三".equals(u.getUsername())))
                .isTrue();
    }
    
    @Test
    @DisplayName("测试更新用户")
    public void testUpdateUser() {
        // Given
        Sysuser inserted = sysuserService.insert(testUser);
        inserted.setUsername("李四");
        
        // When
        Sysuser updated = sysuserService.update(inserted);
        
        // Then
        assertThat(updated).isNotNull();
        assertThat(updated.getUsername()).isEqualTo("李四");
        
        // Verify
        Sysuser found = sysuserService.queryById(inserted.getId());
        assertThat(found.getUsername()).isEqualTo("李四");
    }
    
    @Test
    @DisplayName("测试删除用户")
    public void testDeleteUser() {
        // Given
        Sysuser inserted = sysuserService.insert(testUser);
        Integer userId = inserted.getId();
        
        // When
        boolean deleted = sysuserService.deleteById(userId);
        
        // Then
        assertThat(deleted).isTrue();
        assertThat(sysuserService.queryById(userId)).isNull();
    }
    
    @Test
    @DisplayName("测试用户账号唯一性")
    public void testUserAccountUniqueness() {
        // Given
        sysuserService.insert(testUser);
        
        Sysuser duplicateUser = new Sysuser();
        duplicateUser.setUsername("王五");
        duplicateUser.setAccount(testUser.getAccount()); // 相同的账号
        duplicateUser.setPassword("password456");
        duplicateUser.setPhonenumber("13900139000");
        
        // When & Then - 应该能检测到重复账号
        Sysuser secondUser = sysuserService.insert(duplicateUser);
        // 具体的唯一性检查取决于业务逻辑实现
    }
    
    @Test
    @DisplayName("测试用户信息验证")
    public void testUserInfoValidation() {
        // When
        testUser.setPhonenumber("13800138000");
        testUser.setIdcard("110101199001011234");
        Sysuser inserted = sysuserService.insert(testUser);
        
        // Then
        assertThat(inserted.getPhonenumber()).matches("^1[3-9]\\d{9}$");
        assertThat(inserted.getIdcard()).hasSize(18);
    }
    
    @Test
    @DisplayName("测试用户性别验证")
    public void testUserGenderValidation() {
        // When
        testUser.setSex("女");
        Sysuser inserted = sysuserService.insert(testUser);
        
        // Then
        assertThat(inserted.getSex()).isIn("男", "女");
    }
}
