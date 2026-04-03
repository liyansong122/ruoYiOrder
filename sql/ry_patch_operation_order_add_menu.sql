-- ----------------------------
-- 仅适用于：曾执行「旧版」ry_operation_center.sql（仅有 2017 列表、2018 导出，无 2019）的数据库
-- 若已执行新版脚本（含 2018=订单新增、2019=订单导出），请勿执行本文件
-- 效果：2018 导出排序改为 3，并增加 2019「订单新增」权限
-- ----------------------------

UPDATE sys_menu SET order_num = 3 WHERE menu_id = '2018' AND perms = 'operation:order:export';

INSERT INTO sys_menu VALUES('2019', '订单新增', '2011', '2', '#', '', 'F', '0', '1', 'operation:order:add', '#', 'admin', sysdate(), '', NULL, '');
