-- 为用户表添加省市区和经纬度字段
ALTER TABLE sysuser 
ADD COLUMN province VARCHAR(50) COMMENT '省份' AFTER address,
ADD COLUMN city VARCHAR(50) COMMENT '城市' AFTER province,
ADD COLUMN district VARCHAR(50) COMMENT '区县' AFTER city,
ADD COLUMN longitude DOUBLE COMMENT '经度' AFTER district,
ADD COLUMN latitude DOUBLE COMMENT '纬度' AFTER longitude;

-- 为服务商表添加省市区字段（经纬度已存在）
ALTER TABLE company 
ADD COLUMN province VARCHAR(50) COMMENT '省份' AFTER latitude,
ADD COLUMN city VARCHAR(50) COMMENT '城市' AFTER province,
ADD COLUMN district VARCHAR(50) COMMENT '区县' AFTER city;

-- 为用户表添加位置索引
CREATE INDEX idx_sysuser_location ON sysuser(longitude, latitude);

-- 为用户表添加省市区索引
CREATE INDEX idx_sysuser_address ON sysuser(province, city, district);

-- 为服务商表添加省市区索引
CREATE INDEX idx_company_address ON company(province, city, district);

