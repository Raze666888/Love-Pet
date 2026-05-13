package com.example.service;

import com.javaPro.myProject.modules.company.service.CompanyService;
import com.javaPro.myProject.modules.product.dao.ProductDao;
import com.javaPro.myProject.modules.product.entity.Product;
import com.javaPro.myProject.modules.product.service.impl.ProductServiceImpl;
import com.javaPro.myProject.modules.userlike.dao.UserlikeDao;
import com.javaPro.myProject.modules.sysuser.service.SysuserService;
import com.javaPro.myProject.modules.sysuser.entity.Sysuser;
import com.javaPro.myProject.modules.company.entity.Company;
import com.javaPro.myProject.modules.order.dao.OrderDao;
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

/**
 * 商品服务测试类
 * 修复：添加缺失的 Mock 依赖（SysuserService, OrderDao）
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private UserlikeDao userlikeDao;

    @Mock
    private CompanyService companyService;

    @Mock
    private SysuserService sysuserService;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Company testCompany;
    private Sysuser testUser;

    @BeforeEach
    void setUp() {
        // 初始化测试商品
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setProductname("Test Product");
        testProduct.setKedanjia("99.99");
        testProduct.setImg("test-image.jpg");
        testProduct.setDetailimg("[\"detail1.jpg\", \"detail2.jpg\"]");
        testProduct.setCompanyid(1);
        testProduct.setDetailImgList(Arrays.asList("detail1.jpg", "detail2.jpg"));

        // 初始化测试公司
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setCreateid(1);
        testCompany.setAvgRating(4.5);
        testCompany.setRatingCount(10);
        testCompany.setServiceArea("北京市");

        // 初始化测试用户
        testUser = new Sysuser();
        testUser.setId(1);
        testUser.setUsername("测试服务商");
    }

    @Test
    void testQueryById_Success() {
        // Given
        when(productDao.queryById(1)).thenReturn(testProduct);
        when(companyService.queryById(1)).thenReturn(testCompany);
        when(sysuserService.queryById(1)).thenReturn(testUser);
        when(orderDao.getOrderQuantityByProductId(1)).thenReturn(5L);

        // When
        Product result = productService.queryById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getProductname()).isEqualTo("Test Product");
        assertThat(result.getDetailImgList()).hasSize(2);
        assertThat(result.getCompanyName()).isEqualTo("测试服务商");
        assertThat(result.getCompanyRating()).isEqualTo(4.5);
        verify(productDao).queryById(1);
    }

    @Test
    void testQueryById_NotFound() {
        // Given
        when(productDao.queryById(999)).thenReturn(null);

        // When
        Product result = productService.queryById(999);

        // Then
        assertThat(result).isNull();
        verify(productDao).queryById(999);
    }

    @Test
    void testQueryByPage_Success() {
        // Given
        List<Product> productList = Arrays.asList(testProduct);
        when(productDao.queryAllByLimit(any(Product.class))).thenReturn(productList);
        when(orderDao.getOrderQuantityByProductId(1)).thenReturn(3L);

        // When
        List<Product> result = productService.queryByPage(new Product());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductname()).isEqualTo("Test Product");
        verify(productDao).queryAllByLimit(any(Product.class));
    }

    @Test
    void testInsert_Success() {
        // Given
        when(productDao.insert(any(Product.class))).thenReturn(1);

        // When
        Product result = productService.insert(testProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductname()).isEqualTo("Test Product");
        verify(productDao).insert(testProduct);
    }

    @Test
    void testUpdate_Success() {
        // Given
        when(productDao.update(any(Product.class))).thenReturn(1);
        when(productDao.queryById(testProduct.getId())).thenReturn(testProduct);
        when(companyService.queryById(1)).thenReturn(testCompany);
        when(sysuserService.queryById(1)).thenReturn(testUser);
        when(orderDao.getOrderQuantityByProductId(1)).thenReturn(5L);

        // When
        Product result = productService.update(testProduct);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProductname()).isEqualTo("Test Product");
        verify(productDao).update(testProduct);
        verify(productDao).queryById(testProduct.getId());
    }

    @Test
    void testDeleteById_Success() {
        // Given
        when(productDao.deleteById(anyInt())).thenReturn(1);

        // When
        boolean result = productService.deleteById(1);

        // Then
        assertThat(result).isTrue();
        verify(productDao).deleteById(1);
    }

    @Test
    void testDeleteById_Failed() {
        // Given
        when(productDao.deleteById(anyInt())).thenReturn(0);

        // When
        boolean result = productService.deleteById(999);

        // Then
        assertThat(result).isFalse();
        verify(productDao).deleteById(999);
    }

    @Test
    void testQueryById_WithNullDetailImg() {
        // Given
        testProduct.setDetailimg(null);
        testProduct.setDetailImgList(null);
        when(productDao.queryById(1)).thenReturn(testProduct);
        when(companyService.queryById(1)).thenReturn(testCompany);
        when(sysuserService.queryById(1)).thenReturn(testUser);
        when(orderDao.getOrderQuantityByProductId(1)).thenReturn(0L);

        // When
        Product result = productService.queryById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDetailImgList()).isNull();
        verify(productDao).queryById(1);
    }

    @Test
    void testQueryById_WithNullCompany() {
        // Given
        testProduct.setCompanyid(null);
        when(productDao.queryById(1)).thenReturn(testProduct);
        when(orderDao.getOrderQuantityByProductId(1)).thenReturn(0L);

        // When
        Product result = productService.queryById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCompanyid()).isNull();
        verify(productDao).queryById(1);
    }
}
