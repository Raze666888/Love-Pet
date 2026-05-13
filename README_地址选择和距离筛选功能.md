# 地址选择和距离筛选功能 - 实现总结

## 📋 功能概述

本次更新为宠物服务平台添加了三个核心功能：

### 1️⃣ 省市区联动地址选择器
- ✅ 用户和服务商注册/编辑时使用省市区三级联动选择
- ✅ 输入详细地址后自动定位到地图
- ✅ 支持点击地图调整位置
- ✅ 自动保存省市区和经纬度信息

### 2️⃣ 距离筛选功能
- ✅ 在宠物服务列表页面按距离筛选
- ✅ 支持1/3/5/10/20公里内筛选
- ✅ 支持自定义距离筛选
- ✅ 自动计算并显示用户与服务商的距离

### 3️⃣ 服务商地图视图
- ✅ 在宠物服务列表页面新增地图视图
- ✅ 以用户位置为中心显示所有服务商
- ✅ 鼠标悬停/点击查看服务商详情
- ✅ 显示服务商名称、地址、距离、评分等信息

## 📁 文件变更清单

### 后端文件

#### 实体类
- ✅ `src/main/java/com/javaPro/myProject/modules/sysuser/entity/Sysuser.java`
  - 新增：province, city, district, longitude, latitude

- ✅ `src/main/java/com/javaPro/myProject/modules/company/entity/Company.java`
  - 新增：province, city, district, distance, distanceText

- ✅ `src/main/java/com/javaPro/myProject/modules/product/entity/Product.java`
  - 新增：companyLongitude, companyLatitude, distance, distanceText

#### MyBatis映射文件
- ✅ `src/main/resources/mapper/SysuserDao.xml`
  - 更新：resultMap和所有查询语句

- ✅ `src/main/resources/mapper/CompanyDao.xml`
  - 更新：resultMap和所有查询语句

#### 工具类
- ✅ `src/main/java/com/javaPro/myProject/common/utils/DistanceUtil.java`（新增）
  - 距离计算（Haversine公式）
  - 距离格式化
  - 坐标验证

#### 数据库迁移
- ✅ `src/main/resources/db/migration/add_address_fields.sql`（新增）
  - 为sysuser表添加省市区和经纬度字段
  - 为company表添加省市区字段
  - 创建相关索引

### 前端文件

#### JavaScript组件
- ✅ `src/main/resources/static/js/address-selector.js`（新增）
  - 省市区联动选择器
  - 地图显示和交互
  - 地理编码和逆地理编码

#### 文档
- ✅ `docs/地址选择和距离筛选功能实现说明.md`（新增）
  - 完整的功能说明
  - 数据库变更说明
  - 后端实现说明
  - 前端实现说明

- ✅ `docs/shop.html更新代码片段.md`（新增）
  - shop.html的详细更新步骤
  - 完整的代码片段
  - Vue实例更新说明

- ✅ `README_地址选择和距离筛选功能.md`（本文件）
  - 功能总结
  - 快速开始指南

## 🚀 快速开始

### 第一步：执行数据库迁移

```bash
mysql -u root -p chongwufuwu < src/main/resources/db/migration/add_address_fields.sql
```

或者手动执行SQL：

```sql
-- 为用户表添加字段
ALTER TABLE sysuser 
ADD COLUMN province VARCHAR(50) COMMENT '省份' AFTER address,
ADD COLUMN city VARCHAR(50) COMMENT '城市' AFTER province,
ADD COLUMN district VARCHAR(50) COMMENT '区县' AFTER city,
ADD COLUMN longitude DOUBLE COMMENT '经度' AFTER district,
ADD COLUMN latitude DOUBLE COMMENT '纬度' AFTER longitude;

-- 为服务商表添加字段
ALTER TABLE company 
ADD COLUMN province VARCHAR(50) COMMENT '省份' AFTER latitude,
ADD COLUMN city VARCHAR(50) COMMENT '城市' AFTER province,
ADD COLUMN district VARCHAR(50) COMMENT '区县' AFTER city;

-- 创建索引
CREATE INDEX idx_sysuser_location ON sysuser(longitude, latitude);
CREATE INDEX idx_sysuser_address ON sysuser(province, city, district);
CREATE INDEX idx_company_address ON company(province, city, district);
```

### 第二步：更新shop.html

参考 `docs/shop.html更新代码片段.md` 文件，按照以下步骤更新：

1. 在head部分添加高德地图API引用
2. 在筛选条件区域添加距离筛选
3. 添加视图切换按钮
4. 添加服务商地图视图容器
5. 更新Vue实例（data、methods、mounted）
6. 添加CSS样式

### 第三步：重启项目并测试

```bash
# 重新编译项目
mvn clean package

# 启动项目
java -jar target/myJavaProject.jar
```

### 第四步：测试功能

1. **测试地址选择器**
   - 访问个人信息页面
   - 编辑地址信息
   - 选择省市区并输入详细地址
   - 点击定位按钮查看地图

2. **测试距离筛选**
   - 访问宠物服务列表页面
   - 允许浏览器获取位置权限
   - 在距离筛选下拉框中选择距离范围
   - 查看筛选结果

3. **测试服务商地图**
   - 访问宠物服务列表页面
   - 点击"全部服务商数据"按钮
   - 查看地图上的服务商分布
   - 点击标记查看服务商详情

## 🎯 核心功能说明

### 1. 地址选择器使用

```javascript
// 创建地址选择器实例
const addressSelector = new AddressSelector({
    containerId: 'address-container',
    onAddressSelected: function(addressData) {
        // addressData包含：
        // - province: 省份
        // - city: 城市
        // - district: 区县
        // - detailedAddress: 详细地址
        // - fullAddress: 完整地址
        // - longitude: 经度
        // - latitude: 纬度
        console.log('选择的地址：', addressData);
    }
});

// 设置初始地址（可选）
addressSelector.setAddress({
    province: '北京市',
    city: '北京市',
    district: '朝阳区',
    detailedAddress: 'xxx街道xxx号',
    longitude: 116.397428,
    latitude: 39.90923
});
```

### 2. 距离计算

```java
// 后端Java代码
import com.javaPro.myProject.common.utils.DistanceUtil;

// 计算两点间距离
double distance = DistanceUtil.calculateDistance(
    userLat, userLon,
    providerLat, providerLon
);

// 格式化距离显示
String distanceText = DistanceUtil.formatDistance(distance);
// 输出示例："500米" 或 "2.5公里" 或 "15公里"

// 判断是否在范围内
boolean isNearby = DistanceUtil.isWithinDistance(
    userLat, userLon,
    providerLat, providerLon,
    5.0 // 5公里范围内
);
```

```javascript
// 前端JavaScript代码
// 计算距离
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // 地球半径（公里）
    const dLat = toRadians(lat2 - lat1);
    const dLon = toRadians(lon2 - lon1);
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
}

// 格式化距离
function formatDistance(distance) {
    if (distance < 1) {
        return Math.round(distance * 1000) + '米';
    } else if (distance < 10) {
        return distance.toFixed(1) + '公里';
    } else {
        return Math.round(distance) + '公里';
    }
}
```

### 3. 服务商地图显示

```javascript
// 初始化地图
const map = new AMap.Map('providers-map', {
    zoom: 13,
    center: [userLongitude, userLatitude]
});

// 添加用户位置标记（蓝色）
const userMarker = new AMap.Marker({
    position: [userLongitude, userLatitude],
    map: map,
    icon: new AMap.Icon({
        image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png',
        size: new AMap.Size(25, 34)
    }),
    title: '我的位置'
});

// 添加服务商位置标记（红色）
providers.forEach(provider => {
    const marker = new AMap.Marker({
        position: [provider.longitude, provider.latitude],
        map: map,
        icon: new AMap.Icon({
            image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
            size: new AMap.Size(19, 31)
        }),
        title: provider.username
    });
    
    // 添加信息窗体
    const infoWindow = new AMap.InfoWindow({
        content: `<div>服务商信息...</div>`
    });
    
    marker.on('click', function() {
        infoWindow.open(map, marker.getPosition());
    });
});
```

## ⚠️ 注意事项

### 1. 浏览器定位权限
- 用户需要授权浏览器获取位置信息
- 建议在HTTPS环境下使用（localhost除外）
- 首次访问时会弹出权限请求

### 2. 高德地图API Key
- 确保API Key已正确配置
- 建议设置域名白名单
- 注意API调用次数限制

### 3. 数据准确性
- 地址越详细，定位越准确
- 建议用户输入完整的街道和门牌号
- 定位精度受环境和设备影响

### 4. 性能优化
- 大量标记点时建议使用聚合显示
- 避免频繁调用地理编码API
- 优先使用已保存的经纬度数据

## 🔧 故障排除

### 问题1：地图无法显示
**解决方案：**
- 检查API Key是否正确
- 检查网络连接
- 查看浏览器控制台错误信息

### 问题2：无法获取用户位置
**解决方案：**
- 检查浏览器是否支持定位
- 确认用户已授权位置权限
- 使用HTTPS或localhost访问

### 问题3：距离计算不准确
**解决方案：**
- 确认经纬度数据正确
- 检查坐标系统（高德使用GCJ-02）
- 验证Haversine公式实现

### 问题4：地址定位失败
**解决方案：**
- 使用更详细的地址描述
- 确保地址在中国境内
- 检查地理编码API调用是否成功

## 📚 相关文档

- [高德地图JavaScript API文档](https://lbs.amap.com/api/javascript-api/summary)
- [地址选择和距离筛选功能实现说明](docs/地址选择和距离筛选功能实现说明.md)
- [shop.html更新代码片段](docs/shop.html更新代码片段.md)
- [高德地图集成说明](docs/高德地图集成说明.md)

## 🎨 界面预览

### 地址选择器
- 省市区三级联动下拉框
- 详细地址输入框
- 定位按钮
- 地图显示（400px高度）
- 当前位置信息提示

### 距离筛选
- 距离范围下拉框
- 自定义距离输入
- 筛选结果实时更新

### 服务商地图
- 全屏地图视图（600px高度）
- 用户位置标记（蓝色）
- 服务商位置标记（红色）
- 信息窗体（点击显示）
- 标记动画效果（鼠标悬停）

## 🚀 后续优化建议

1. **路线规划**：添加从用户位置到服务商的路线规划
2. **标记聚合**：服务商较多时使用聚合显示
3. **热力图**：显示服务商分布热力图
4. **实时定位**：支持用户位置实时更新
5. **离线缓存**：缓存地图数据，减少API调用
6. **多条件组合**：距离+评分+价格等多条件筛选
7. **地址历史**：保存用户常用地址
8. **周边搜索**：搜索周边的宠物医院、宠物店等

## 📝 更新日志

### v1.0.0 (2025-10-07)
- ✅ 实现省市区联动地址选择器
- ✅ 实现距离筛选功能
- ✅ 实现服务商地图视图
- ✅ 添加距离计算工具类
- ✅ 更新数据库结构
- ✅ 完善文档和示例代码

## 👥 技术支持

如有问题，请查看：
- 项目文档：`docs/` 目录
- 高德地图官方文档
- 浏览器控制台错误信息

---

**开始使用**：按照"快速开始"部分的步骤操作

**需要帮助**：查看"故障排除"部分或相关文档

