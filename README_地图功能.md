# 宠物服务平台 - 高德地图功能

## 📍 功能概述

本项目已成功集成高德地图API，实现了以下两个核心功能：

### 1️⃣ 用户端 - 服务商位置查看
在宠物服务详情页面，用户可以：
- ✅ 查看服务商的详细地址
- ✅ 在地图上查看服务商的精确位置
- ✅ 通过地图标记查看服务商信息

### 2️⃣ 服务商端 - 位置管理
服务商在个人信息页面可以：
- ✅ 查看当前注册地址和位置地图
- ✅ 查看经纬度坐标信息
- ✅ 使用"重新定位"功能自动获取当前位置
- ✅ 自动保存位置信息到数据库

## 🚀 快速开始

### 第一步：获取API Key
1. 访问 [高德开放平台](https://lbs.amap.com/)
2. 注册并创建应用
3. 获取Web端(JS API)的Key

### 第二步：配置API Key
编辑文件：`src/main/resources/static/js/map-config.js`
```javascript
const AMAP_CONFIG = {
    key: '你的API_Key',  // 替换这里
    // ...
};
```

### 第三步：更新数据库
```bash
mysql -u root -p chongwufuwu < src/main/resources/db/migration/add_company_location_fields.sql
```

### 第四步：测试功能
启动项目后访问：
- 测试页面：http://localhost:7002/web/test-map
- 用户端：http://localhost:7002/web/userproductlist
- 服务商端：http://localhost:7002/web/personal

## 📁 项目结构

```
myJavaProject/
├── src/main/java/
│   └── com/javaPro/myProject/modules/
│       ├── company/
│       │   └── entity/Company.java          # 添加了经纬度字段
│       └── web/WebController.java            # 添加了测试路由
├── src/main/resources/
│   ├── mapper/
│   │   └── CompanyDao.xml                    # 更新了SQL映射
│   ├── static/js/
│   │   └── map-config.js                     # 🆕 地图配置文件
│   ├── templates/
│   │   ├── webSite/
│   │   │   └── single-product.html           # 添加了服务商地图
│   │   ├── personal.html                     # 添加了位置管理
│   │   └── test-map.html                     # 🆕 地图测试页面
│   └── db/migration/
│       └── add_company_location_fields.sql   # 🆕 数据库迁移脚本
└── docs/
    ├── 高德地图集成说明.md                    # 🆕 详细文档
    ├── 快速配置高德地图.md                    # 🆕 快速指南
    └── 地图功能实现总结.md                    # 🆕 实现总结
```

## 🎯 功能演示

### 用户查看服务商位置
```
用户浏览服务 → 点击服务详情 → 查看服务商详情 → 查看位置地图
```

### 服务商管理位置
```
服务商登录 → 个人信息 → 查看位置地图 → 重新定位（可选）
```

## 🛠️ 技术实现

### 核心技术
- **地图服务**：高德地图 JavaScript API 2.0
- **前端框架**：Vue.js 2.x + Element UI
- **后端框架**：Spring Boot 2.7.18
- **数据库**：MySQL 8.0

### 关键功能
- ✅ 地理编码（地址 → 坐标）
- ✅ 逆地理编码（坐标 → 地址）
- ✅ 浏览器定位
- ✅ 地图标记和信息窗体
- ✅ 位置数据持久化

## 📊 数据库变更

新增字段：
```sql
company表:
  - longitude (DOUBLE)  # 经度
  - latitude (DOUBLE)   # 纬度
  - INDEX idx_company_location (longitude, latitude)
```

## 📖 文档导航

- **快速开始**：[快速配置高德地图.md](docs/快速配置高德地图.md) - 5分钟配置指南
- **详细文档**：[高德地图集成说明.md](docs/高德地图集成说明.md) - 完整的功能说明
- **实现总结**：[地图功能实现总结.md](docs/地图功能实现总结.md) - 技术实现细节

## 🧪 测试

访问测试页面：http://localhost:7002/web/test-map

测试项目：
1. ✅ 基础地图显示
2. ✅ 地理编码（地址转坐标）
3. ✅ 逆地理编码（坐标转地址）
4. ✅ 浏览器定位
5. ✅ 距离计算

## ⚠️ 注意事项

1. **API Key安全**
   - 不要将API Key提交到公开仓库
   - 建议在高德控制台设置域名白名单

2. **浏览器权限**
   - 定位功能需要用户授权
   - 建议使用HTTPS或localhost

3. **地址格式**
   - 地址需要详细且准确
   - 必须是中国境内的地址

## 🔧 常见问题

### Q: 地图无法显示？
**A:** 检查API Key配置和网络连接

### Q: 定位不准确？
**A:** 确保使用HTTPS，在户外或窗边定位

### Q: 地址无法转换？
**A:** 使用更详细的地址描述

详细问题解答请查看：[快速配置高德地图.md](docs/快速配置高德地图.md)

## 🎨 界面预览

### 服务商详情地图
- 显示服务商位置
- 地图标记点击显示详细信息
- 地址信息展示

### 服务商位置管理
- 当前位置地图展示
- 经纬度坐标显示
- 重新定位按钮
- 自动保存功能

## 🚀 后续优化

计划中的功能：
- [ ] 路线规划（用户到服务商）
- [ ] 周边服务商搜索
- [ ] 地图范围筛选
- [ ] 距离显示和排序
- [ ] 服务商分布热力图

## 📞 技术支持

- 高德地图官方文档：https://lbs.amap.com/api/javascript-api/summary
- 高德地图示例：https://lbs.amap.com/demo/list/js-api
- 项目文档：`docs/` 目录

## 📝 更新日志

### v1.0.0 (2025-10-07)
- ✅ 集成高德地图API
- ✅ 实现服务商位置展示
- ✅ 实现服务商位置管理
- ✅ 添加浏览器定位功能
- ✅ 完善文档和测试页面

## 👥 贡献

欢迎提交Issue和Pull Request！

## 📄 许可

本项目使用的高德地图API需要遵守高德开放平台的使用协议。

---

**开始使用**：查看 [快速配置高德地图.md](docs/快速配置高德地图.md)

**需要帮助**：查看 [高德地图集成说明.md](docs/高德地图集成说明.md)

