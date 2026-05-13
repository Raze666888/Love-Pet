package com.javaPro.myProject.modules.product.service;

import com.javaPro.myProject.config.BaseUnitTest;
import com.javaPro.myProject.modules.product.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 产品服务单元测试
 */
@DisplayName("产品服务单元测试")
public class ProductServiceTest extends BaseUnitTest {
    
    @Autowired
    private ProductService productService;
    
    private Product testProduct;
    
    @BeforeEach
    public void setUp() {
        testProduct = new Product();
        testProduct.setProductname("宠物美容服务");
        testProduct.setProductdes("专业宠物美容护理");
        testProduct.setImg("beauty.jpg");
        testProduct.setChengben("50.00");
        testProduct.setKedanjia("100.00");
        testProduct.setKucun("10");
        testProduct.setFahuotianshu("1");
        testProduct.setChandi("北京市朝阳区");
    }
    
    @Test
    @DisplayName("测试插入产品")
    public void testInsertProduct() {
        // When
        Product inserted = productService.insert(testProduct);
        
        // Then
        assertThat(inserted).isNotNull();
        assertThat(inserted.getId()).isNotNull();
        assertThat(inserted.getProductname()).isEqualTo("宠物美容服务");
    }
    
    @Test
    @DisplayName("测试根据ID查询产品")
    public void testQueryProductById() {
        // Given
        Product inserted = productService.insert(testProduct);
        Integer productId = inserted.getId();
        
        // When
        Product found = productService.queryById(productId);
        
        // Then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(productId);
        assertThat(found.getProductname()).isEqualTo("宠物美容服务");
    }
    
    @Test
    @DisplayName("测试查询不存在的产品")
    public void testQueryNonExistentProduct() {
        // When
        Product found = productService.queryById(9999);
        
        // Then
        assertThat(found).isNull();
    }
    
    @Test
    @DisplayName("测试分页查询产品")
    public void testQueryProductsByPage() {
        // Given
        productService.insert(testProduct);
        Product query = new Product();
        query.setProductname("宠物美容服务");
        
        // When
        List<Product> products = productService.queryByPage(query);
        
        // Then
        assertThat(products).isNotEmpty();
        assertThat(products.stream()
                .anyMatch(p -> "宠物美容服务".equals(p.getProductname())))
                .isTrue();
    }
    
    @Test
    @DisplayName("测试更新产品")
    public void testUpdateProduct() {
        // Given
        Product inserted = productService.insert(testProduct);
        inserted.setProductname("更新的宠物美容服务");
        
        // When
        Product updated = productService.update(inserted);
        
        // Then
        assertThat(updated).isNotNull();
        assertThat(updated.getProductname()).isEqualTo("更新的宠物美容服务");
        
        // Verify
        Product found = productService.queryById(inserted.getId());
        assertThat(found.getProductname()).isEqualTo("更新的宠物美容服务");
    }
    
    @Test
    @DisplayName("测试删除产品")
    public void testDeleteProduct() {
        // Given
        Product inserted = productService.insert(testProduct);
        Integer productId = inserted.getId();
        
        // When
        boolean deleted = productService.deleteById(productId);
        
        // Then
        assertThat(deleted).isTrue();
        assertThat(productService.queryById(productId)).isNull();
    }
    
    @Test
    @DisplayName("测试产品价格验证")
    public void testProductPriceValidation() {
        // When & Then
        testProduct.setKedanjia("100.50");
        Product inserted = productService.insert(testProduct);
        
        assertThat(inserted.getKedanjia()).isEqualTo("100.50");
    }
    
    @Test
    @DisplayName("测试产品库存验证")
    public void testProductInventoryValidation() {
        // When
        testProduct.setKucun("5");
        Product inserted = productService.insert(testProduct);
        
        // Then
        assertThat(inserted.getKucun()).isEqualTo("5");
        assertThat(Integer.parseInt(inserted.getKucun())).isGreaterThan(0);
    }
}
