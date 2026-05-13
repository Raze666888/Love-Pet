-- 数据库初始化脚本
-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS chongwufuwu CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE chongwufuwu;

-- 创建系统用户表
CREATE TABLE IF NOT EXISTS sysuser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) COMMENT '用户名',
    sex VARCHAR(10) COMMENT '性别',
    phonenumber VARCHAR(30) COMMENT '手机号',
    account VARCHAR(100) COMMENT '账号',
    password VARCHAR(255) COMMENT '密码',
    idcard VARCHAR(50) COMMENT '身份证号',
    address VARCHAR(255) COMMENT '地址',
    img VARCHAR(255) COMMENT '头像',
    role VARCHAR(50) COMMENT '角色 1-管理员 2-用户 3-服务商',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    age INT COMMENT '年龄',
    remark VARCHAR(255) COMMENT '备注',
    money DOUBLE DEFAULT 0 COMMENT '余额',
    petname VARCHAR(100) COMMENT '宠物名',
    petage VARCHAR(50) COMMENT '宠物年龄',
    petdes VARCHAR(500) COMMENT '宠物描述',
    pettype VARCHAR(100) COMMENT '宠物类型',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    longitude DOUBLE COMMENT '经度',
    latitude DOUBLE COMMENT '纬度'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 创建公司/服务商表
CREATE TABLE IF NOT EXISTS company (
    id INT AUTO_INCREMENT PRIMARY KEY,
    companyname VARCHAR(255) COMMENT '公司名称',
    phonenumber VARCHAR(30) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    zhuceshijian TIMESTAMP COMMENT '注册时间',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status VARCHAR(50) COMMENT '状态',
    createid INT COMMENT '创建人ID',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    longitude DOUBLE COMMENT '经度',
    latitude DOUBLE COMMENT '纬度',
    avg_rating DOUBLE DEFAULT 0 COMMENT '平均评分',
    rating_count INT DEFAULT 0 COMMENT '评分次数',
    service_area VARCHAR(255) COMMENT '服务区域'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司/服务商表';

-- 创建商品类型表
CREATE TABLE IF NOT EXISTS producttype (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(100) COMMENT '类型名称',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status VARCHAR(50) COMMENT '状态',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类型表';

-- 创建商品表
CREATE TABLE IF NOT EXISTS product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productname VARCHAR(255) COMMENT '商品名称',
    productdes VARCHAR(500) COMMENT '商品描述',
    img VARCHAR(255) COMMENT '商品图片',
    detailimg VARCHAR(255) COMMENT '详情图片',
    chengben VARCHAR(50) COMMENT '成本',
    kedanjia VARCHAR(50) COMMENT '客单价',
    kucun VARCHAR(50) COMMENT '库存',
    fahuotianshu VARCHAR(50) COMMENT '发货天数',
    chandi VARCHAR(255) COMMENT '产地',
    guige VARCHAR(255) COMMENT '规格',
    companyid INT COMMENT '公司ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status VARCHAR(50) COMMENT '状态',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    producttype INT COMMENT '商品类型',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2',
    service_start_time VARCHAR(20) COMMENT '服务开始时间',
    service_end_time VARCHAR(20) COMMENT '服务结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 创建购物车表
CREATE TABLE IF NOT EXISTS shopcart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productid INT COMMENT '商品ID',
    userid INT COMMENT '用户ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    number INT COMMENT '数量',
    totalprice DECIMAL(10,2) COMMENT '总价',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `order` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carid VARCHAR(255) COMMENT '购物车ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    userid INT COMMENT '用户ID',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2',
    remark VARCHAR(255) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 创建订单评价表
CREATE TABLE IF NOT EXISTS order_evalute (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1000) COMMENT '评价内容',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    userid INT COMMENT '用户ID',
    orderid INT COMMENT '订单ID',
    companyid INT COMMENT '公司ID',
    rating DOUBLE COMMENT '评分'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';

-- 创建消息表
CREATE TABLE IF NOT EXISTS message (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(2000) COMMENT '消息内容',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    senderid INT COMMENT '发送者ID',
    receiveid INT COMMENT '接收者ID',
    status VARCHAR(50) COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- 创建类型表
CREATE TABLE IF NOT EXISTS t_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    typename VARCHAR(100) COMMENT '类型名称',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status VARCHAR(50) COMMENT '状态',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类型表';

-- 创建网站公告表
CREATE TABLE IF NOT EXISTS webnotice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) COMMENT '标题',
    type INT COMMENT '类型',
    content TEXT COMMENT '内容',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status VARCHAR(50) COMMENT '状态',
    img VARCHAR(255) COMMENT '图片'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网站公告表';

-- 创建充值表
CREATE TABLE IF NOT EXISTS tmoney (
    id INT AUTO_INCREMENT PRIMARY KEY,
    money DECIMAL(10,2) COMMENT '金额',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    audittime TIMESTAMP COMMENT '审核时间',
    auditstatus VARCHAR(50) COMMENT '审核状态',
    cause VARCHAR(500) COMMENT '原因',
    userid INT COMMENT '用户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值表';

-- 创建用户收藏表
CREATE TABLE IF NOT EXISTS userlike (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userid INT COMMENT '用户ID',
    productid INT COMMENT '商品ID',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    spare1 VARCHAR(255) COMMENT '备用字段1',
    spare2 VARCHAR(255) COMMENT '备用字段2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 创建评论表
CREATE TABLE IF NOT EXISTS comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pid INT COMMENT '父评论ID',
    userid INT COMMENT '用户ID',
    content VARCHAR(2000) COMMENT '内容',
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updatetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status VARCHAR(50) COMMENT '状态',
    type VARCHAR(50) COMMENT '类型',
    to_user_id INT COMMENT '回复用户ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 插入默认数据
INSERT INTO sysuser (username, account, password, role, phonenumber) VALUES 
('管理员', 'admin', 'admin123', '1', '13800138000');

INSERT INTO producttype (type_name, status) VALUES 
('宠物美容', '1'),
('宠物寄养', '1'),
('宠物训练', '1'),
('宠物医疗', '1');
