# 自动化测试指南

## 概述
本项目包含完整的自动化测试框架，包括单元测试、集成测试和API测试。

## 测试结构

```
src/test/
├── java/
│   └── com/javaPro/myProject/
│       ├── config/                  # 测试基类和配置
│       │   ├── BaseUnitTest.java
│       │   └── BaseIntegrationTest.java
│       ├── utils/
│       │   └── TestUtils.java       # 测试工具类
│       ├── modules/
│       │   ├── product/
│       │   │   └── service/
│       │   │       └── ProductServiceTest.java
│       │   ├── order/
│       │   │   └── service/
│       │   │       └── OrderServiceTest.java
│       │   └── sysuser/
│       │       └── service/
│       │           └── SysuserServiceTest.java
│       └── controller/               # API集成测试
│           ├── ProductControllerIntegrationTest.java
│           ├── OrderControllerIntegrationTest.java
│           ├── SysuserControllerIntegrationTest.java
│           ├── ShopcartControllerIntegrationTest.java
│           ├── CommentControllerIntegrationTest.java
│           ├── WebnoticeControllerIntegrationTest.java
│           └── MessageControllerIntegrationTest.java
└── resources/
    ├── application-test.properties  # 测试环境配置
    └── application-test.yml         # 测试环境YAML配置
```

## 本地运行测试

### 1. 单元测试
运行所有单元测试：
```bash
mvn clean test -DskipITs
```

运行特定测试类：
```bash
mvn test -Dtest=ProductServiceTest
```

运行特定测试方法：
```bash
mvn test -Dtest=ProductServiceTest#testInsertProduct
```

### 2. 集成测试
运行所有集成测试（包括单元测试）：
```bash
mvn clean verify
```

仅运行集成测试：
```bash
mvn test -Dtest=*IT
```

### 3. API测试
运行API集成测试：
```bash
mvn test -Dtest=*IntegrationTest
```

### 4. 所有测试
运行完整的测试套件：
```bash
mvn clean verify
```

## 测试覆盖率

查看代码覆盖率报告：
```bash
mvn clean verify
# 报告位置: target/site/jacoco/index.html
```

## 测试依赖

项目使用以下测试框架：
- **JUnit 5**: 单元测试框架
- **Mockito**: Mock对象
- **AssertJ**: 流式断言
- **REST Assured**: API测试
- **Testcontainers**: 容器化测试环境
- **H2 Database**: 内存数据库
- **Spring Boot Test**: Spring集成测试

## 测试命名规范

- **单元测试**: `*Test.java` (例如: `ProductServiceTest.java`)
- **集成测试**: `*IT.java` (例如: `ProductServiceIT.java`)
- **API测试**: `*IntegrationTest.java` (例如: `ProductControllerIntegrationTest.java`)

## 测试环境配置

### 单元测试
使用H2内存数据库，无需外部依赖：
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
```

### 集成测试
使用Docker容器化的MySQL和Redis：
- MySQL: localhost:3306
- Redis: localhost:6379

## GitHub Actions CI/CD

### 工作流说明
1. **Unit Tests**: 运行单元测试
2. **Integration Tests**: 运行集成测试
3. **API Tests**: 运行API接口测试
4. **Code Quality**: 运行代码质量检查

### 工作流文件
- `.github/workflows/ci-cd.yml`: 主要CI/CD流程
- `.github/workflows/e2e-tests.yml`: 端到端测试（定时任务）

### 运行条件
- Push到 `main` 或 `develop` 分支
- PR到 `main` 分支
- 手动触发 (`workflow_dispatch`)

## 最佳实践

### 1. 编写好的单元测试
```java
@Test
@DisplayName("测试产品插入")
public void testInsertProduct() {
    // Arrange: 准备测试数据
    Product product = new Product();
    product.setProductname("测试产品");
    
    // Act: 执行测试
    Product result = productService.insert(product);
    
    // Assert: 验证结果
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotNull();
}
```

### 2. 使用描述性的测试名称
- 清晰表达测试的意图
- 使用 `@DisplayName` 注解
- 遵循 "given-when-then" 模式

### 3. 隔离测试
- 每个测试应该独立运行
- 使用 `@BeforeEach` 初始化测试数据
- 使用 Mock 隔离外部依赖

### 4. 测试数据管理
- 使用测试配置文件
- 利用数据库容器
- 避免依赖外部系统

## 常见问题

### Q: 如何调试测试？
A: 在IDE中运行测试，设置断点后以Debug模式运行。

### Q: 如何跳过某些测试？
A: 
```bash
mvn test -DskipTests
# 或
mvn test -Dtest=ProductServiceTest -DskipITs
```

### Q: 如何提高测试速度？
A: 
- 使用并行执行（已在pom.xml中配置）
- 减少数据库操作
- 使用Mock代替真实对象

### Q: 如何查看测试报告？
A: 
- 单元测试报告: `target/surefire-reports/`
- 覆盖率报告: `target/site/jacoco/`
- GitHub Actions: Check runs in Pull Request

## 相关资源

- [JUnit 5文档](https://junit.org/junit5/docs/)
- [Mockito文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [REST Assured](https://rest-assured.io/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [Testcontainers](https://www.testcontainers.org/)

## 维护和扩展

### 添加新的测试模块
1. 在 `src/test/java` 中创建对应的包结构
2. 继承 `BaseUnitTest` 或 `BaseIntegrationTest`
3. 遵循测试命名规范
4. 使用 `@DisplayName` 添加描述

### 更新测试配置
修改 `src/test/resources/application-test.properties` 或 `application-test.yml`

### 扩展测试框架
在 `TestUtils.java` 中添加新的工具方法

---

**最后更新**: 2026-05-09
**维护者**: 开发团队
