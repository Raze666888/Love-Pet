-- 为company表添加经纬度字段
-- 用于存储服务商的地理位置信息，配合高德地图API使用

ALTER TABLE company 
ADD COLUMN longitude DOUBLE COMMENT '经度' AFTER service_area,
ADD COLUMN latitude DOUBLE COMMENT '纬度' AFTER longitude;

-- 为经纬度字段添加索引以提高查询性能
CREATE INDEX idx_company_location ON company(longitude, latitude);

