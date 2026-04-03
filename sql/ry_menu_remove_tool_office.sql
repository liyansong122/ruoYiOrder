-- ----------------------------
-- 移除：系统工具、若依官网（含子菜单与代码生成按钮）
-- 实例演示 不在数据库，由 application.yml 中 ruoyi.demoEnabled 控制（已默认 false）
-- 在库 ry 中执行一次即可
-- ----------------------------

DELETE FROM sys_role_menu WHERE menu_id IN (
  '3','4','114','115','116','1057','1058','1059','1060','1061'
);

DELETE FROM sys_menu WHERE menu_id IN (
  '1057','1058','1059','1060','1061','114','115','116','4','3'
);

-- 若已添加运营中心，可将一级排序改为 3（可选）
-- UPDATE sys_menu SET order_num = 3 WHERE menu_id = '2000';
