# 测试快速参考卡片

## 🚀 快速开始

### 本地运行测试
```bash
# 运行所有测试
mvn clean verify

# 只运行单元测试
mvn clean test -DskipITs

# 只运行集成测试
mvn test -Dtest=*IT

# 只运行API测试
mvn test -Dtest=*IntegrationTest

# 跳过所有测试
mvn clean install -DskipTests
```

## 📋 常用命令

### 运行特定测试
```bash
# 运行某个测试类
mvn test -Dtest=ProductServiceTest

# 运行某个测试方法
mvn test -Dtest=ProductServiceTest#testInsertProduct

# 运行多个测试类
mvn test -Dtest=Product*Test

# 运行特定包下的测试
mvn test -Dtest=com.javaPro.myProject.modules.product.*Test
```

### 查看覆盖率
```bash
# 生成覆盖率报告
mvn clean verify

# 在浏览器中查看报告
open target/site/jacoco/index.html
```

## 🧪 测试类型

| 类型 | 文件模式 | 命令 | 用途 |
|------|--------|------|------|
| 单元测试 | `*Test.java` | `mvn test -DskipITs` | 测试单个方法或类 |
| 集成测试 | `*IT.java` | `mvn test -Dtest=*IT` | 测试多个组件的交互 |
| API测试 | `*IntegrationTest.java` | `mvn test -Dtest=*IntegrationTest` | 测试HTTP接口 |

## 📊 GitHub Actions

### 工作流文件
- `ci-cd.yml` - 主CI/CD流程（push/PR触发）
- `e2e-tests.yml` - 端到端测试（定时任务）

### 查看工作流
```bash
# 列出工作流
gh workflow list

# 查看最近的运行
gh run list

# 查看特定运行的日志
gh run view <RUN_ID>

# 手动触发工作流
gh workflow run ci-cd.yml
```

## ✅ 测试清单

### 添加新测试前
- [ ] 选择正确的测试类型（单元/集成/API）
- [ ] 继承正确的基类（`BaseUnitTest` 或 `BaseIntegrationTest`）
- [ ] 使用 `@DisplayName` 注解
- [ ] 遵循 "Given-When-Then" 模式
- [ ] 添加适当的断言

### 提交前
- [ ] 所有本地测试通过
- [ ] 覆盖率满足要求（建议 >70%）
- [ ] 没有跳过的测试
- [ ] 代码遵循命名规范

## 🔧 常见问题

| 问题 | 解决 |
|------|------|
| 数据库连接失败 | 检查MySQL容器是否运行；查看application-test.properties配置 |
| 测试超时 | 增加测试超时时间；检查系统性能 |
| Maven缓存问题 | 删除 `~/.m2/repository`；或使用 `mvn clean` |
| 权限拒绝 | 检查文件权限；确保驱动程序可执行 |
| 端口被占用 | 更改端口配置；关闭占用的进程 |

## 📁 文件结构
```
src/test/
├── java/
│   └── com/javaPro/myProject/
│       ├── config/
│       │   ├── BaseUnitTest.java          ← 继承这个
│       │   └── BaseIntegrationTest.java   ← 或这个
│       ├── modules/*/service/
│       │   └── *ServiceTest.java          ← 单元测试
│       └── controller/
│           └── *IntegrationTest.java      ← API测试
└── resources/
    ├── application-test.properties        ← 测试配置
    ├── TEST_GUIDE.md                      ← 详细指南
    └── test-data.sql                      ← 测试数据
```

## 📝 测试模板

### 单元测试
```java
@Test
@DisplayName("描述你的测试")
public void testSomething() {
    // Arrange: 准备
    Entity entity = new Entity();
    
    // Act: 执行
    Result result = service.method(entity);
    
    // Assert: 验证
    assertThat(result).isNotNull();
}
```

### API测试
```java
@Test
@DisplayName("测试API端点")
public void testEndpoint() {
    given()
        .log().all()
        .contentType("application/json")
    .when()
        .get("/api/endpoint")
    .then()
        .log().all()
        .statusCode(200);
}
```

## 🎯 目标

- **单元测试覆盖率**: > 70%
- **集成测试覆盖率**: > 50%
- **API测试覆盖率**: 所有公开端点

## 📞 获取帮助

1. 查看 `TEST_GUIDE.md` 获取详细信息
2. 查看 `GITHUB_ACTIONS_GUIDE.md` 了解CI/CD
3. 查看测试样板文件进行参考

## 🔗 快速链接

- [JUnit 5文档](https://junit.org/junit5/)
- [AssertJ文档](https://assertj.github.io/assertj-doc.html)
- [REST Assured](https://rest-assured.io/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

---
**版本**: 1.0 | **更新**: 2026-05-09
