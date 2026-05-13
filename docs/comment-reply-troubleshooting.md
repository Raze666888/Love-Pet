# 评论回复功能问题排查指南

## 🔧 已修复的问题

### 1. GET请求参数传递问题
**问题**: 前端调用GET接口时使用了`data`参数，应该使用URL查询字符串
**修复**: 修改了`loadComments()`方法中的API调用

### 2. POST请求JSON序列化问题
**问题**: 后端控制器使用`@RequestParam`，前端发送表单数据格式不匹配
**修复**: 后端改为`@RequestBody`，前端添加`contentType: "application/json"`并使用`JSON.stringify()`

### 3. 数据库字段映射问题
**问题**: Comment实体缺少`replies`和`replyToUser`字段
**修复**: 添加了必要的字段和SQL查询

## 🚀 测试修复后的功能

### 方法1: 使用测试页面
1. 启动Spring Boot应用
2. 访问 `http://localhost:7002/static/test-comment-api.html`
3. 按顺序测试连接、获取评论树、发表回复功能

### 方法2: 直接在公告详情页面测试
1. 访问任意公告详情页面
2. 尝试发表评论和回复
3. 检查浏览器开发者工具的Network面板

## 🔍 常见问题和解决方案

### 问题1: 网络连接失败
**症状**: `ERR_CONNECTION_REFUSED` 或类似错误
**解决方案**:
- 确保Spring Boot应用正在运行
- 检查端口7002是否被占用
- 验证防火墙设置

### 问题2: CORS跨域问题
**症状**: 浏览器控制台显示跨域错误
**解决方案**:
- 在`WebMvcConfiguration.java`中添加CORS配置
- 或在`application.properties`中添加`spring.web.cors.allowed-origins=*`

### 问题3: 404 Not Found
**症状**: API路径找不到
**解决方案**:
- 确认控制器映射路径正确: `@RequestMapping("comment")`
- 检查方法路径: `@GetMapping("tree/{noticeId}")`等
- 验证Spring Boot应用扫描了正确的包

### 问题4: 400 Bad Request
**症状**: 请求参数格式错误
**解决方案**:
- 检查前端发送的JSON格式
- 确认后端接收的字段名匹配
- 验证必需字段是否为空

### 问题5: 500 Internal Server Error
**症状**: 服务器内部错误
**解决方案**:
- 查看Spring Boot控制台日志
- 检查数据库连接配置
- 验证SQL语句语法

## 🛠️ 数据库相关问题

### 检查comment表是否存在
```sql
USE chongwufuwu;
DESCRIBE comment;
```

### 检查测试数据
```sql
SELECT * FROM comment WHERE notice_id = 1;
```

### 检查外键约束
```sql
SHOW CREATE TABLE comment;
```

## 📝 日志调试

### 启用MyBatis日志
在`application.properties`中确认以下配置：
```properties
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

### 查看Spring Boot日志
检查控制台输出，特别关注：
- HTTP请求映射
- SQL执行情况
- 异常堆栈信息

## 🔄 重启和重新部署

1. 停止Spring Boot应用
2. 重新编译和打包: `mvn clean package`
3. 重新启动应用
4. 清除浏览器缓存

## 📞 技术支持

如果问题仍然存在，请提供以下信息：
1. 浏览器控制台错误信息
2. Spring Boot应用日志
3. 网络面板的请求和响应详情
4. 具体操作步骤和期望结果

## ✅ 验证修复成功

当看到以下情况时，说明修复成功：
1. 评论列表能正常加载
2. 能成功发表新评论
3. 能对评论进行回复
4. 回复内容能正确显示层级关系
5. 用户标识正确显示
6. 删除功能正常工作