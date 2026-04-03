-- 补充订单导入按钮权限（已部署运营中心菜单库执行一次即可）
INSERT INTO sys_menu VALUES('2020', '订单导入', '2011', '4', '#', '', 'F', '0', '1', 'operation:order:import', '#', 'admin', sysdate(), '', NULL, '');
