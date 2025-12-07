-- 菜单 SQL
insert into study_java_sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('操作日志', '2', '1', 'operlog', 'monitor/operlog/index', 0, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', sysdate(), '', null, '操作日志菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into study_java_sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('操作日志查询', @parentId, '1', '#', '', 0, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', sysdate(), '', null, '');

insert into study_java_sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('操作日志删除', @parentId, '2', '#', '', 0, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', sysdate(), '', null, '');

insert into study_java_sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('操作日志清空', @parentId, '3', '#', '', 0, 0, 'F', '0', '0', 'monitor:operlog:clean', '#', 'admin', sysdate(), '', null, '');
