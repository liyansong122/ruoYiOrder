-- ----------------------------
-- 运营中心：商品表、订单表、菜单（与系统管理/系统监控同级）
-- 在库 ry 中执行；若菜单已存在请先调整或删除冲突记录
-- ----------------------------

-- 商品表
DROP TABLE IF EXISTS ops_order;
DROP TABLE IF EXISTS ops_product;
CREATE TABLE ops_product (
  product_id      bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '商品ID',
  product_code    varchar(64)     NOT NULL                   COMMENT '商品编码',
  product_name    varchar(200)    NOT NULL                   COMMENT '商品名称',
  price           decimal(12,2)   NOT NULL DEFAULT 0.00      COMMENT '单价',
  stock           int(11)         NOT NULL DEFAULT 0         COMMENT '库存',
  status          char(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
  create_by       varchar(64)     DEFAULT ''                 COMMENT '创建者',
  create_time     datetime                                   COMMENT '创建时间',
  update_by       varchar(64)     DEFAULT ''                 COMMENT '更新者',
  update_time     datetime                                   COMMENT '更新时间',
  remark          varchar(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (product_id),
  UNIQUE KEY uk_ops_product_code (product_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='运营-商品表';

-- 订单表
CREATE TABLE ops_order (
  order_id        bigint(20)      NOT NULL AUTO_INCREMENT    COMMENT '订单ID',
  order_no        varchar(32)     NOT NULL                   COMMENT '订单号',
  product_id      bigint(20)      DEFAULT NULL               COMMENT '商品ID',
  buyer_name      varchar(64)     DEFAULT ''                 COMMENT '购买人',
  amount          decimal(12,2)   NOT NULL DEFAULT 0.00      COMMENT '订单金额',
  order_status    char(1)         DEFAULT '0'                COMMENT '状态（0待支付 1已支付 2已取消）',
  create_time     datetime                                   COMMENT '创建时间',
  remark          varchar(500)    DEFAULT NULL               COMMENT '备注',
  PRIMARY KEY (order_id),
  UNIQUE KEY uk_ops_order_no (order_no),
  KEY idx_ops_order_product (product_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='运营-订单表';

-- 示例数据
INSERT INTO ops_product VALUES (1, 'SKU001', '示例商品A', 99.00, 100, '0', 'admin', sysdate(), '', NULL, '示例');
INSERT INTO ops_product VALUES (2, 'SKU002', '示例商品B', 199.50, 50, '0', 'admin', sysdate(), '', NULL, NULL);
INSERT INTO ops_order VALUES (1, 'ORD202603220001', 1, '张三', 99.00, '1', sysdate(), NULL);
INSERT INTO ops_order VALUES (2, 'ORD202603220002', 2, '李四', 199.50, '0', sysdate(), NULL);

-- 一级目录：运营中心（与系统管理、系统监控同级，显示顺序 3）
INSERT INTO sys_menu VALUES('2000', '运营中心', '0', '3', '#', '', 'M', '0', '1', '', 'fa fa-shopping-bag', 'admin', sysdate(), '', NULL, '运营中心目录');
-- 二级菜单
INSERT INTO sys_menu VALUES('2010', '商品维护', '2000', '1', '/operation/product', '', 'C', '0', '1', 'operation:product:view', 'fa fa-cube', 'admin', sysdate(), '', NULL, '商品维护');
INSERT INTO sys_menu VALUES('2011', '订单查询', '2000', '2', '/operation/order', '', 'C', '0', '1', 'operation:order:view', 'fa fa-list-alt', 'admin', sysdate(), '', NULL, '订单查询');

-- 商品维护-按钮
INSERT INTO sys_menu VALUES('2012', '商品查询', '2010', '1', '#', '', 'F', '0', '1', 'operation:product:list', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2013', '商品新增', '2010', '2', '#', '', 'F', '0', '1', 'operation:product:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2014', '商品修改', '2010', '3', '#', '', 'F', '0', '1', 'operation:product:edit', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2015', '商品删除', '2010', '4', '#', '', 'F', '0', '1', 'operation:product:remove', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2016', '商品导出', '2010', '5', '#', '', 'F', '0', '1', 'operation:product:export', '#', 'admin', sysdate(), '', NULL, '');

-- 订单查询-按钮
INSERT INTO sys_menu VALUES('2017', '订单列表', '2011', '1', '#', '', 'F', '0', '1', 'operation:order:list', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2018', '订单新增', '2011', '2', '#', '', 'F', '0', '1', 'operation:order:add', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2019', '订单导出', '2011', '3', '#', '', 'F', '0', '1', 'operation:order:export', '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu VALUES('2020', '订单导入', '2011', '4', '#', '', 'F', '0', '1', 'operation:order:import', '#', 'admin', sysdate(), '', NULL, '');
