CREATE TABLE IF NOT EXISTS sysuser (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100),
  sex VARCHAR(10),
  phonenumber VARCHAR(30),
  account VARCHAR(100),
  password VARCHAR(255),
  idcard VARCHAR(50),
  address VARCHAR(255),
  img VARCHAR(255),
  role VARCHAR(50),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  age INT,
  remark VARCHAR(255),
  money DOUBLE,
  petname VARCHAR(100),
  petage VARCHAR(50),
  petdes VARCHAR(500),
  pettype VARCHAR(100),
  province VARCHAR(50),
  city VARCHAR(50),
  district VARCHAR(50),
  longitude DOUBLE,
  latitude DOUBLE
);

CREATE TABLE IF NOT EXISTS company (
  id INT AUTO_INCREMENT PRIMARY KEY,
  companyname VARCHAR(255),
  phonenumber VARCHAR(30),
  address VARCHAR(255),
  zhuceshijian TIMESTAMP,
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  status VARCHAR(50),
  createid INT,
  province VARCHAR(50),
  city VARCHAR(50),
  district VARCHAR(50),
  longitude DOUBLE,
  latitude DOUBLE,
  avg_rating DOUBLE,
  rating_count INT,
  service_area VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS producttype (
  id INT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(100),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  status VARCHAR(50),
  spare1 VARCHAR(255),
  spare2 VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  productname VARCHAR(255),
  productdes VARCHAR(500),
  img VARCHAR(255),
  detailimg VARCHAR(255),
  chengben VARCHAR(50),
  kedanjia VARCHAR(50),
  kucun VARCHAR(50),
  fahuotianshu VARCHAR(50),
  chandi VARCHAR(255),
  guige VARCHAR(255),
  companyid INT,
  createtime TIMESTAMP,
  status VARCHAR(50),
  updatetime TIMESTAMP,
  producttype INT,
  spare1 VARCHAR(255),
  spare2 VARCHAR(255),
  service_start_time VARCHAR(20),
  service_end_time VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS shopcart (
  id INT AUTO_INCREMENT PRIMARY KEY,
  productid INT,
  userid INT,
  createtime TIMESTAMP,
  number INT,
  totalprice DECIMAL(10,2),
  spare1 VARCHAR(255),
  spare2 VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `order` (
  id INT AUTO_INCREMENT PRIMARY KEY,
  carid VARCHAR(255),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  userid INT,
  spare1 VARCHAR(255),
  spare2 VARCHAR(255),
  remark VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS order_evalute (
  id INT AUTO_INCREMENT PRIMARY KEY,
  content VARCHAR(1000),
  createtime TIMESTAMP,
  userid INT,
  orderid INT,
  companyid INT,
  rating DOUBLE
);

CREATE TABLE IF NOT EXISTS message (
  id INT AUTO_INCREMENT PRIMARY KEY,
  content VARCHAR(2000),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  senderid INT,
  receiveid INT,
  status VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS t_type (
  id INT AUTO_INCREMENT PRIMARY KEY,
  typename VARCHAR(100),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  status VARCHAR(50),
  spare1 VARCHAR(255),
  spare2 VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS webnotice (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  type INT,
  content CLOB,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  status VARCHAR(50),
  img VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS tmoney (
  id INT AUTO_INCREMENT PRIMARY KEY,
  money DECIMAL(10,2),
  createtime TIMESTAMP,
  audittime TIMESTAMP,
  auditstatus VARCHAR(50),
  cause VARCHAR(500),
  userid INT
);

CREATE TABLE IF NOT EXISTS userlike (
  id INT AUTO_INCREMENT PRIMARY KEY,
  userid INT,
  productid INT,
  createtime TIMESTAMP,
  spare1 VARCHAR(255),
  spare2 VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS comment (
  id INT AUTO_INCREMENT PRIMARY KEY,
  pid INT,
  userid INT,
  content VARCHAR(2000),
  createtime TIMESTAMP,
  updatetime TIMESTAMP,
  status VARCHAR(50),
  type VARCHAR(50),
  to_user_id INT
);
