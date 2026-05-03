-- ----------------------------
-- 订单付款状态追踪改造脚本
-- 1. ops_order_item 增加 paid_quantity / paid_amount 字段
-- 2. ops_order 增加 paid_amount 字段
-- ----------------------------

-- 1. 订单明细增加已付数量和已付金额
ALTER TABLE ops_order_item
  ADD COLUMN paid_quantity INT NOT NULL DEFAULT 0 COMMENT '已付数量' AFTER quantity,
  ADD COLUMN paid_amount   DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '已付金额' AFTER paid_quantity;

-- 2. 订单主表增加已付金额（待付金额 = amount - paid_amount，不单独存列）
ALTER TABLE ops_order
  ADD COLUMN paid_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '已付金额' AFTER amount;

-- 3. 回填：将已有明细的已付数量=总数量、已付金额=小计金额（历史数据视为全额已付）
UPDATE ops_order_item SET paid_quantity = quantity, paid_amount = amount WHERE paid_quantity = 0;

-- 4. 回填：将已有订单的已付金额=总金额（历史数据视为全额已付）
UPDATE ops_order SET paid_amount = amount WHERE paid_amount = 0;
