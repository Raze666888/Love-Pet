# 宠物服务平台 (Chongwufuwu)

基于 Spring Boot 的宠物服务综合平台，提供宠物美容、寄养、训练、医疗等服务的在线预约与管理功能，集成 AI 智能宠物咨询。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.18 |
| 持久层 | MyBatis + PageHelper |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis (Spring Data Redis + Jedis) |
| 前端模板 | Thymeleaf + hAdmin 后台管理 UI |
| 前端组件 | Vue.js、jQuery、Bootstrap、Element UI、Layui |
| AI 集成 | DeepSeek API（智能宠物咨询）+ Tavily（实时搜索） |
| 监控 | Spring Boot Actuator + Prometheus + Grafana |
| 容器化 | Docker + Docker Compose + Nginx 反向代理 |
| CI/CD | GitHub Actions |
| 测试 | JUnit 5 + Mockito + REST Assured + Selenium + TestContainers + JaCoCo |
| 负载测试 | Apache Benchmark (AB) + JMeter |

## 功能模块

### 用户端
- **用户注册/登录** — 支持管理员、普通用户、服务商三种角色
- **服务浏览与搜索** — 按类型、距离、评分筛选宠物服务
- **地图定位** — 基于经纬度的地址选择与距离筛选
- **购物车与下单** — 服务加入购物车、生成订单
- **订单评价** — 对已完成订单进行评分和评论
- **评论回复** — 支持嵌套评论与回复
- **消息系统** — 用户间站内消息
- **充值功能** — 账户余额充值与审核
- **收藏功能** — 收藏喜欢的服务
- **AI 宠物咨询** — 基于 DeepSeek 的智能宠物问答

### 服务商端
- **服务商入驻** — 注册公司/服务商信息
- **服务管理** — 发布、编辑宠物服务项目
- **订单处理** — 查看和处理用户订单
- **评分查看** — 查看用户评价与平均评分

### 管理端
- **用户管理** — 管理所有注册用户
- **服务商管理** — 审核与管理服务商
- **服务分类管理** — 管理宠物服务类型
- **公告管理** — 发布和管理网站公告
- **充值审核** — 审核用户充值申请
- **数据统计** — 平台运营数据概览

## 项目结构

```
src/main/java/com/javaPro/myProject/
├── SchedulingApplication.java          # 启动类
├── common/                             # 公共模块
│   ├── co/                             # 常量定义
│   ├── config/                         # Web 配置
│   ├── controller/                     # 基础控制器
│   ├── handle/                         # 拦截器（登录校验）
│   ├── model/                          # 通用响应模型
│   └── util/                           # 工具类
├── controller/                         # 控制器层
├── modules/                            # 业务模块
│   ├── aipet/                          # AI 宠物咨询
│   ├── comment/                        # 评论系统
│   ├── company/                        # 服务商管理
│   ├── login/                          # 登录认证
│   ├── message/                        # 消息系统
│   ├── order/                          # 订单管理
│   ├── orderEvalute/                   # 订单评价
│   ├── product/                        # 服务/商品管理
│   ├── producttype/                    # 服务分类
│   ├── provider/                       # 服务商入口
│   ├── shopcart/                       # 购物车
│   ├── sysuser/                        # 系统用户
│   ├── tmoney/                         # 充值管理
│   ├── type/                           # 类型管理
│   ├── userlike/                       # 用户收藏
│   ├── web/                            # 前台页面
│   └── webnotice/                      # 公告管理
└── service/                            # 文件上传服务

src/main/resources/
├── application.properties              # 主配置文件
├── mapper/                             # MyBatis XML 映射
├── db/migration/                       # 数据库迁移脚本
├── static/                             # 静态资源
│   ├── hAdmin/                         # 后台管理 UI
│   ├── DemoBootStrap/                  # Bootstrap 前端组件
│   ├── element/                        # Element UI
│   ├── font-awesome/                   # Font Awesome 图标
│   ├── css/                            # 自定义样式
│   └── img/                            # 图片资源
└── templates/                          # Thymeleaf 模板
    ├── webSite/                        # 前台页面
    ├── company/                        # 服务商管理页
    ├── order/                          # 订单管理页
    ├── product/                        # 服务管理页
    └── ...                             # 其他管理页面
```

## 快速开始

### 环境要求

- JDK 8
- Maven 3.6+
- MySQL 8.0
- Redis 6.0+

### 1. 初始化数据库

```bash
mysql -u root -p < scripts/init-db.sql
```

### 2. 修改配置

编辑 `src/main/resources/application.properties`，修改数据库和 Redis 连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chongwufuwu?serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=你的密码
spring.redis.host=localhost
spring.redis.port=6379
```

### 3. 启动项目

```bash
mvn clean package -DskipTests
java -jar target/myJavaProject-0.0.1-SNAPSHOT.jar
```

启动后访问：`http://localhost:7002`

默认管理员账号：`admin` / `admin123`

## Docker 部署

### 一键启动（开发环境）

```bash
docker-compose up -d
```

### 部署脚本

```bash
# 开发环境
./deploy.sh -e dev

# 生产环境
./deploy.sh -e prod -t v1.0.0
```

### 环境配置

| 文件 | 说明 |
|------|------|
| `docker-compose.yml` | 开发环境 |
| `docker-compose.dev.yml` | 测试环境 |
| `docker-compose.prod.yml` | 生产环境 |
| `application-docker.yml` | Docker 环境配置 |

## 测试

### 运行自动化测试

```bash
# 运行单元测试
mvn test -DskipITs

# 运行集成测试
mvn test -Dspring.profiles.active=test

# 运行全部测试并生成覆盖率报告
mvn test
# 覆盖率报告：target/site/jacoco/index.html

# 运行 Selenium 测试
mvn test -P selenium-tests
```

### 负载测试

```bash
# 使用 Apache Benchmark
load-test/ab-test-scripts.bat

# 使用 JMeter
# 在 JMeter 中打开 load-test/load-test-plan.jmx 运行
```

## 监控

- **健康检查**：`http://localhost:7002/actuator/health`
- **Prometheus 指标**：`http://localhost:7002/actuator/prometheus`

## 数据库表结构

| 表名 | 说明 |
|------|------|
| sysuser | 系统用户表（管理员/用户/服务商） |
| company | 服务商表 |
| product | 服务/商品表 |
| producttype | 服务分类表 |
| shopcart | 购物车表 |
| order | 订单表 |
| order_evalute | 订单评价表 |
| message | 消息表 |
| webnotice | 公告表 |
| tmoney | 充值记录表 |
| userlike | 用户收藏表 |
| comment | 评论表 |
| t_type | 类型表 |
