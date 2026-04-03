-- ----------------------------
-- 订单表字段扩展脚本
-- 添加：地区、电话、发货物流、厂家、单价、数量、下单日期
-- ----------------------------

-- 添加新字段
ALTER TABLE ops_order 
ADD COLUMN region VARCHAR(200) DEFAULT NULL COMMENT '地区',
ADD COLUMN phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
ADD COLUMN logistics VARCHAR(100) DEFAULT NULL COMMENT '发货物流',
ADD COLUMN factory VARCHAR(100) DEFAULT NULL COMMENT '厂家',
ADD COLUMN unit_price DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
ADD COLUMN quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
ADD COLUMN order_date DATE DEFAULT NULL COMMENT '下单日期';

-- 修改备注：下单时间默认为订单新增日期（由程序处理）
-- 下单日期如果不填写，默认使用当前日期
