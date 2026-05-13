# 负载测试指南 - 宠物服务平台

## 📋 目录
1. [测试前准备](#测试前准备)
2. [方法一：使用 JMeter (推荐)](#方法一使用-jmeter-推荐)
3. [方法二：使用 Java 测试类](#方法二使用-java-测试类)
4. [方法三：使用 ApacheBench 快速测试](#方法三使用-apachebench-快速测试)
5. [测试结果解读](#测试结果解读)
6. [常见问题](#常见问题)

---

## 测试前准备

### 1. 确保服务运行
```bash
# 启动你的 Spring Boot 应用，确保运行在 localhost:7002
cd c:\Users\26819\Desktop\大三上Group7\myJavaProject
mvn spring-boot:run
```

### 2. 验证服务可用
在浏览器访问：
- http://localhost:7002/ (首页)
- http://localhost:7002/product (产品列表)

### 3. 检查依赖
确保 pom.xml 包含 REST Assured (项目已有)：
```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 方法一：使用 JMeter (推荐)

### 下载安装 JMeter
1. 访问 https://jmeter.apache.org/download_jmeter.cgi
2. 下载 `apache-jmeter-5.x.zip`
3. 解压到任意目录

### 运行测试

#### 方式 1: 图形界面运行
```
1. 双击 bin/jmeter.bat 启动 JMeter
2. 点击菜单: 文件 -> 打开
3. 选择项目中的文件: load-test/load-test-plan.jmx
4. 修改测试参数（如需要）：
   - BASE_URL: http://localhost:7002
   - THREAD_COUNT: 100 (并发用户数)
   - RAMP_UP: 10 (启动时间秒数)
   - DURATION: 60 (持续时间秒)
5. 点击绿色运行按钮 ▶
```

#### 方式 2: 命令行运行（无界面）
```bash
cd c:\Users\26819\Desktop\大三上Group7\myJavaProject\load-test

# Windows
jmeter.bat -n -t load-test-plan.jmx -l results.jtl -e -o output

# 或直接双击运行
load-test-run.bat
```

### JMeter 测试场景说明

| 场景 | 接口 | 说明 |
|------|------|------|
| 场景1 | POST /toLogin | 用户登录 |
| 场景2 | GET /product | 产品分页列表 |
| 场景3 | GET /product/detail | 产品详情查询 |
| 场景4 | GET /product/filterServices | 服务筛选查询 |
| 场景5 | GET / | 首页访问 |

### JMeter 参数调整说明

| 参数 | 默认值 | 说明 |
|------|--------|------|
| BASE_URL | http://localhost:7002 | 测试的目标地址 |
| THREAD_COUNT | 100 | 并发用户数 |
| RAMP_UP | 10 | 所有用户启动时间（秒） |
| DURATION | 60 | 测试持续时间（秒） |

**建议测试参数：**
- 轻量测试：10 用户，10 秒启动，持续 30 秒
- 标准测试：50 用户，10 秒启动，持续 60 秒
- 压力测试：100 用户，10 秒启动，持续 120 秒

---

## 方法二：使用 Java 测试类

### 运行负载测试
```bash
cd c:\Users\26819\Desktop\大三上Group7\myJavaProject

# 方式1: 使用 Maven 运行
mvn test -Dtest=LoadTestRunner -Dspring.profiles.active=test

# 方式2: 运行指定测试
mvn test -Dtest=LoadTestRunner#loadTestProductList
```

### Java 测试类测试场景

| 测试方法 | 说明 | 并发数 |
|---------|------|--------|
| warmUp() | 预热测试 | 10 |
| loadTestProductList() | 产品列表接口 | 50用户x10请求 |
| loadTestServiceFilter() | 服务筛选接口 | 30用户x5请求 |
| spikeLoadTest() | 峰值测试 | 100用户x3请求 |

### 测试输出示例
```
========== 负载测试: 产品列表 ==========
并发用户数: 50
每个用户请求数: 10
总请求数: 500

========== 产品列表查询 测试结果 ==========
测试持续时间: 2345 ms
成功请求: 500
失败请求: 0
吞吐量: 213.22 req/s

响应时间统计:
  平均响应时间: 45.67 ms
  最小响应时间: 12 ms
  最大响应时间: 156 ms
```

---

## 方法三：使用 ApacheBench 快速测试

### 安装 ApacheBench

#### Windows
1. 安装 XAMPP 或 WAMP（包含 ab 命令）
2. 或从 https://www.apache.org/ 获取

#### macOS
```bash
# 已内置
```

#### Linux
```bash
sudo apt install apache2-utils  # Ubuntu/Debian
sudo yum install httpd-tools      # CentOS/RHEL
```

### 快速测试命令

```bash
# 1. 首页 - 100并发，1000请求
ab -n 1000 -c 100 http://localhost:7002/

# 2. 产品列表 - 50并发，500请求
ab -n 500 -c 50 "http://localhost:7002/product?pageNum=1&pageSize=10"

# 3. 产品详情 - 50并发，300请求
ab -n 300 -c 50 "http://localhost:7002/product/detail?id=1"

# 4. 服务筛选 - 30并发，200请求
ab -n 200 -c 30 "http://localhost:7002/product/filterServices?minPrice=0&maxPrice=1000"
```

### ab 输出解读
```
Server Software:        Apache-Coyote/1.1
Server Hostname:        localhost
Server Port:            7002

Document Path:          /
Document Length:        1234 bytes

Concurrency Level:      100          # 并发数
Time taken for tests:   5.234 seconds
Complete requests:      1000         # 总请求数
Failed requests:         0           # 失败请求
Requests per second:    191.06 [#/sec]  # 每秒请求数（吞吐量）
Time per request:       523.416 [ms]    # 平均响应时间
Transfer rate:          256.78 [Kbytes/sec]  # 传输速率

Percentage of the requests served within a certain time (ms)
  50%     120                     # 50%请求在120ms内完成
  66%     180
  90%     250
  95%     320
  98%     450
  99%     520
 100%     890                     # 最慢的请求
```

---

## 测试结果解读

### 关键指标说明

| 指标 | 含义 | 良好标准 |
|------|------|---------|
| **吞吐量 (TPS)** | 每秒处理请求数 | 越高越好 |
| **平均响应时间** | 所有请求的平均时间 | < 200ms |
| **P95 响应时间** | 95%请求在此时间内完成 | < 500ms |
| **错误率** | 失败请求占比 | < 1% |
| **最大并发数** | 系统能承受的最大并发 | 需要测试确定 |

### 性能判断标准

| 性能等级 | 吞吐量 | 平均响应 | 说明 |
|---------|--------|---------|------|
| ⭐⭐⭐⭐⭐ 优秀 | > 500 req/s | < 50ms | 系统性能优秀 |
| ⭐⭐⭐⭐ 良好 | 200-500 req/s | 50-100ms | 可接受 |
| ⭐⭐⭐ 一般 | 100-200 req/s | 100-200ms | 需要优化 |
| ⭐⭐ 较差 | 50-100 req/s | 200-500ms | 性能瓶颈明显 |
| ⭐ 很差 | < 50 req/s | > 500ms | 需要立即优化 |

### 常见瓶颈排查

1. **CPU 占用高** → 优化算法，减少不必要的计算
2. **内存占用高** → 检查内存泄漏，增加堆内存
3. **数据库慢** → 优化 SQL，添加索引
4. **连接池耗尽** → 调整连接池大小
5. **响应时间随并发增加** → 存在资源竞争

---

## 常见问题

### Q1: 测试时服务无响应？
```
检查：
1. 服务是否正常运行 (localhost:7002)
2. 数据库连接是否正常
3. 查看服务日志
```

### Q2: 错误率突然增加？
```
可能原因：
1. 连接池耗尽
2. 数据库连接超时
3. 内存不足导致 OOM
4. 并发超出系统承受范围
```

### Q3: JMeter 如何查看详细报告？
```
1. 测试完成后，点击 "Generate HTML Report"
2. 选择之前生成的 .jtl 文件
3. 生成 HTML 格式报告
```

### Q4: 如何模拟真实用户行为？
```
在 JMeter 中：
1. 添加 HTTP Cookie Manager
2. 添加用户定义的变量
3. 使用参数化文件 (CSV)
4. 添加思考时间 (Timer)
```

---

## 📊 你的项目测试配置建议

基于你的项目配置（Tomcat 最大 400 线程），建议测试参数：

| 测试阶段 | 并发数 | 持续时间 | 目标 |
|---------|--------|---------|------|
| 基准测试 | 10 | 30s | 验证正常性能 |
| 负载测试 | 50 | 60s | 验证设计容量 |
| 压力测试 | 100 | 60s | 找出瓶颈 |
| 极限测试 | 200+ | 30s | 确定系统极限 |

---

## 下一步

测试完成后，你可以：
1. 分析性能瓶颈位置
2. 优化数据库查询
3. 调整连接池参数
4. 添加缓存
5. 考虑微服务拆分

如需帮助分析测试结果或制定优化方案，请分享测试输出！
