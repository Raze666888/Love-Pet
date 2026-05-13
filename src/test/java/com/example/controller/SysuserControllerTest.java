package com.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaPro.myProject.modules.sysuser.controller.SysuserController;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SysuserController.class)
@ContextConfiguration(classes = {SysuserController.class})
class SysuserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private SysuserService sysuserService;
    
    @MockBean
    private FileUploadService fileUploadService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
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
    void testQueryByPage_Success() throws Exception {
        // Given
        List<Sysuser> userList = Arrays.asList(testUser);
        when(sysuserService.queryByPage(any(Sysuser.class))).thenReturn(userList);
        
        // When & Then
        mockMvc.perform(get("/sysuser")
                .param("account", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].account").value("testuser"));
    }
    
    @Test
    void testQueryById_Success() throws Exception {
        // Given
        when(sysuserService.queryById(1)).thenReturn(testUser);
        
        // When & Then
        mockMvc.perform(get("/sysuser/detail")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.account").value("testuser"));
    }
    
    @Test
    void testAdd_Success() throws Exception {
        // Given
        when(sysuserService.insert(any(Sysuser.class))).thenReturn(testUser);
        
        // When & Then
        mockMvc.perform(post("/sysuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    @Test
    void testEdit_Success() throws Exception {
        // Given
        when(sysuserService.update(any(Sysuser.class))).thenReturn(testUser);
        
        // When & Then
        mockMvc.perform(put("/sysuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    @Test
    void testEditUserPerson_WithFile() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "avatar.jpg", 
            "image/jpeg", 
            "test image content".getBytes()
        );
        
        when(fileUploadService.uploadFile(any())).thenReturn("https://oss.example.com/avatar.jpg");
        when(sysuserService.update(any(Sysuser.class))).thenReturn(testUser);
        
        // When & Then
        mockMvc.perform(multipart("/sysuser/editUserPerson")
                .file(file)
                .param("id", "1")
                .param("account", "testuser")
                .param("name", "Test User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    @Test
    void testEditUserPerson_WithoutFile() throws Exception {
        // Given
        when(sysuserService.update(any(Sysuser.class))).thenReturn(testUser);
        
        // When & Then
        mockMvc.perform(multipart("/sysuser/editUserPerson")
                .param("id", "1")
                .param("account", "testuser")
                .param("name", "Test User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
    
    @Test
    void testDelete_Success() throws Exception {
        // Given
        when(sysuserService.deleteById(anyInt())).thenReturn(true);
        
        // When & Then
        mockMvc.perform(delete("/sysuser")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
