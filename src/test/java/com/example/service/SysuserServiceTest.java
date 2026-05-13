package com.example.service;

import com.javaPro.myProject.modules.sysuser.dao.SysuserDao;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.impl.SysuserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysuserServiceTest {
    
    @Mock
    private SysuserDao sysuserDao;
    
    @InjectMocks
    private SysuserServiceImpl sysuserService;
    
    private Sysuser testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new Sysuser();
        testUser.setId(1);
        testUser.setAccount("testuser");
        testUser.setPassword("password123");
        testUser.setUsername("Test User");
        testUser.setMoney(100.0);
    }
    
    @Test
    void testQueryById_Success() {
        // Given
        when(sysuserDao.queryById(1)).thenReturn(testUser);
        
        // When
        Sysuser result = sysuserService.queryById(1);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getAccount()).isEqualTo("testuser");
        verify(sysuserDao).queryById(1);
    }
    
    @Test
    void testQueryById_NotFound() {
        // Given
        when(sysuserDao.queryById(999)).thenReturn(null);
        
        // When
        Sysuser result = sysuserService.queryById(999);
        
        // Then
        assertThat(result).isNull();
        verify(sysuserDao).queryById(999);
    }
    
    @Test
    void testQueryByAccount_Success() {
        // Given
        when(sysuserDao.queryByAccount("testuser")).thenReturn(testUser);
        
        // When
        Sysuser result = sysuserService.queryByAccount("testuser");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("testuser");
        verify(sysuserDao).queryByAccount("testuser");
    }
    
    @Test
    void testQueryByPage_Success() {
        // Given
        List<Sysuser> userList = Arrays.asList(testUser);
        when(sysuserDao.queryAllByLimit(any(Sysuser.class))).thenReturn(userList);
        
        // When
        List<Sysuser> result = sysuserService.queryByPage(new Sysuser());
        
        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAccount()).isEqualTo("testuser");
        verify(sysuserDao).queryAllByLimit(any(Sysuser.class));
    }
    
    @Test
    void testInsert_Success() {
        // Given
        when(sysuserDao.insert(any(Sysuser.class))).thenReturn(1);
        
        // When
        Sysuser result = sysuserService.insert(testUser);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("testuser");
        verify(sysuserDao).insert(testUser);
    }
    
    @Test
    void testUpdate_Success() {
        // Given
        when(sysuserDao.update(any(Sysuser.class))).thenReturn(1);
        when(sysuserDao.queryById(testUser.getId())).thenReturn(testUser);

        // When
        Sysuser result = sysuserService.update(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("testuser");
        verify(sysuserDao).update(testUser);
        verify(sysuserDao).queryById(testUser.getId());
    }
    
    @Test
    void testDeleteById_Success() {
        // Given
        when(sysuserDao.deleteById(anyInt())).thenReturn(1);
        
        // When
        boolean result = sysuserService.deleteById(1);
        
        // Then
        assertThat(result).isTrue();
        verify(sysuserDao).deleteById(1);
    }
    
    @Test
    void testDeleteById_Failed() {
        // Given
        when(sysuserDao.deleteById(anyInt())).thenReturn(0);
        
        // When
        boolean result = sysuserService.deleteById(999);
        
        // Then
        assertThat(result).isFalse();
        verify(sysuserDao).deleteById(999);
    }
}
