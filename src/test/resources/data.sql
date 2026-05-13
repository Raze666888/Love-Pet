MERGE INTO sysuser (
  id, username, sex, phonenumber, account, password, idcard, address, role,
  createtime, age, remark, money, province, city, district, longitude, latitude
) KEY(id) VALUES
  (1, 'test-user-1', 'M', '13800000001', 'test_user_1', '123456', '110101199001011111', 'beijing', '2',
   CURRENT_TIMESTAMP, 20, 'seed', 10000, 'beijing', 'beijing', 'chaoyang', 116.40, 39.90);
MERGE INTO sysuser (
  id, username, sex, phonenumber, account, password, idcard, address, role,
  createtime, age, remark, money, province, city, district, longitude, latitude
) KEY(id) VALUES
  (100, 'test-user-100', 'F', '13800000100', 'test_user_100', '123456', '110101199001011100', 'beijing', '2',
   CURRENT_TIMESTAMP, 21, 'seed', 10000, 'beijing', 'beijing', 'haidian', 116.30, 39.96);

MERGE INTO company (
  id, companyname, phonenumber, address, createtime, updatetime, status, createid
) KEY(id) VALUES
  (1, 'test-company', '13900000001', 'beijing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', 1);

MERGE INTO producttype (
  id, type_name, createtime, updatetime, status, spare1, spare2
) KEY(id) VALUES
  (1, 'test-type', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', NULL, NULL);

MERGE INTO product (
  id, productname, productdes, img, detailimg, chengben, kedanjia, kucun, fahuotianshu,
  chandi, guige, companyid, createtime, status, updatetime, producttype, spare1, spare2,
  service_start_time, service_end_time
) KEY(id) VALUES
  (1, 'test-product-1', 'desc', 'a.jpg', 'd1.jpg', '10', '100', '100', '1', 'beijing', 'default', 1, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 1, NULL, NULL, '08:00', '22:00');
MERGE INTO product (
  id, productname, productdes, img, detailimg, chengben, kedanjia, kucun, fahuotianshu,
  chandi, guige, companyid, createtime, status, updatetime, producttype, spare1, spare2,
  service_start_time, service_end_time
) KEY(id) VALUES
  (2, 'test-product-2', 'desc', 'b.jpg', 'd2.jpg', '10', '120', '100', '1', 'beijing', 'default', 1, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 1, NULL, NULL, '08:00', '22:00');
MERGE INTO product (
  id, productname, productdes, img, detailimg, chengben, kedanjia, kucun, fahuotianshu,
  chandi, guige, companyid, createtime, status, updatetime, producttype, spare1, spare2,
  service_start_time, service_end_time
) KEY(id) VALUES
  (3, 'test-product-3', 'desc', 'c.jpg', 'd3.jpg', '10', '130', '100', '1', 'beijing', 'default', 1, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 1, NULL, NULL, '08:00', '22:00');
MERGE INTO product (
  id, productname, productdes, img, detailimg, chengben, kedanjia, kucun, fahuotianshu,
  chandi, guige, companyid, createtime, status, updatetime, producttype, spare1, spare2,
  service_start_time, service_end_time
) KEY(id) VALUES
  (4, 'test-product-4', 'desc', 'd.jpg', 'd4.jpg', '10', '140', '100', '1', 'beijing', 'default', 1, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 1, NULL, NULL, '08:00', '22:00');
MERGE INTO product (
  id, productname, productdes, img, detailimg, chengben, kedanjia, kucun, fahuotianshu,
  chandi, guige, companyid, createtime, status, updatetime, producttype, spare1, spare2,
  service_start_time, service_end_time
) KEY(id) VALUES
  (5, 'test-product-5', 'desc', 'e.jpg', 'd5.jpg', '10', '150', '100', '1', 'beijing', 'default', 1, CURRENT_TIMESTAMP, '1', CURRENT_TIMESTAMP, 1, NULL, NULL, '08:00', '22:00');

MERGE INTO shopcart (
  id, productid, userid, createtime, number, totalprice, spare1, spare2
) KEY(id) VALUES
  (1, 1, 1, CURRENT_TIMESTAMP, 1, 100.00, '0', NULL);
MERGE INTO shopcart (
  id, productid, userid, createtime, number, totalprice, spare1, spare2
) KEY(id) VALUES
  (2, 2, 1, CURRENT_TIMESTAMP, 1, 120.00, '0', NULL);
MERGE INTO shopcart (
  id, productid, userid, createtime, number, totalprice, spare1, spare2
) KEY(id) VALUES
  (3, 3, 1, CURRENT_TIMESTAMP, 1, 130.00, '0', NULL);
MERGE INTO shopcart (
  id, productid, userid, createtime, number, totalprice, spare1, spare2
) KEY(id) VALUES
  (4, 4, 1, CURRENT_TIMESTAMP, 1, 140.00, '0', NULL);
MERGE INTO shopcart (
  id, productid, userid, createtime, number, totalprice, spare1, spare2
) KEY(id) VALUES
  (5, 5, 1, CURRENT_TIMESTAMP, 1, 150.00, '0', NULL);
