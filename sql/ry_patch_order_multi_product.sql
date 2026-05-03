-- ----------------------------
-- 订单支持多商品改造脚本
-- 1. 创建订单明细表 ops_order_item
-- 2. 迁移 ops_order 中的商品字段到 ops_order_item
-- 3. 从 ops_order 移除单一商品字段
-- 4. 从 ops_product 移除单价字段
-- ----------------------------

-- 1. 创建订单明细表
CREATE TABLE ops_order_item (
  item_id       bigint(20)    NOT NULL AUTO_INCREMENT  COMMENT '明细ID',
  order_id      bigint(20)    NOT NULL                 COMMENT '订单ID',
  product_id    bigint(20)    NOT NULL                 COMMENT '商品ID',
  product_name  varchar(200)  NOT NULL                 COMMENT '商品名称（冗余）',
  unit_price    decimal(12,2) NOT NULL DEFAULT 0.00    COMMENT '单价',
  quantity      int(11)       NOT NULL DEFAULT 1       COMMENT '数量',
  amount        decimal(12,2) NOT NULL DEFAULT 0.00    COMMENT '小计金额',
  remark        varchar(500)  DEFAULT NULL             COMMENT '备注',
  PRIMARY KEY (item_id),
  KEY idx_ops_order_item_order_id (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='运营-订单明细表';

-- 2. 迁移已有订单的商品数据到 ops_order_item
INSERT INTO ops_order_item (order_id, product_id, product_name, unit_price, quantity, amount, remark)
SELECT o.order_id, o.product_id, IFNULL(p.product_name, ''), IFNULL(o.unit_price, 0), IFNULL(o.quantity, 1), IFNULL(o.amount, 0), o.remark
FROM ops_order o
LEFT JOIN ops_product p ON o.product_id = p.product_id
WHERE o.product_id IS NOT NULL;

-- 3. 从 ops_order 移除单一商品字段
ALTER TABLE ops_order
  DROP COLUMN product_id,
  DROP COLUMN unit_price,
  DROP COLUMN quantity;

-- 4. 从 ops_product 移除单价字段
ALTER TABLE ops_product
  DROP COLUMN price;
