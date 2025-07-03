create database sysbase charset=utf8 collate=utf8_general_ci;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `type_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型名称',
  `type_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型编码',
  `type_desc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典类型描述',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '部门状态 0-停用 1-启用/正常',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '状态', 'status', NULL, 1, 0, 1, '2025-06-30 11:35:46', NULL, '2025-06-30 16:35:45');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_data_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
  `dict_id` bigint(20) NOT NULL COMMENT '字典ID',
  `dict_data_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典数据名称',
  `dict_data_value` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典数据值',
  `dict_data_desc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值描述',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '字典顺序',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '部门状态 0-停用 1-启用/正常',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`dict_data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '启用', '1', NULL, 0, 1, 0, 1, '2025-06-30 16:53:24', NULL, '2025-06-30 08:53:23');
INSERT INTO `sys_dict_data` VALUES (2, 1, '禁用', '0', NULL, 1, 1, 0, 1, '2025-06-30 16:53:42', NULL, '2025-06-30 08:53:42');

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `login_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '基于IP地址的地理位置',
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录UA',
  `device_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备类型',
  `login_type` int(11) NOT NULL COMMENT '登录类型 1-登录 2-登出',
  `login_type_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录类型描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`login_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统登录日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父菜单ID',
  `menu_type` int(11) NOT NULL COMMENT '菜单类型 1-目录 2-菜单 3-功能',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path_type` int(11) NULL DEFAULT NULL COMMENT '地址类型 1-vue-router 2-外链',
  `path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单地址',
  `perms` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识(menu_type为2/3且非外链时有效)',
  `icon_type` int(11) NULL DEFAULT NULL COMMENT '菜单图标类型 1-ElementPlus原生图标 2-iconify图标',
  `icon` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0-停用 1-启用/正常',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 999, NULL, NULL, NULL, 1, 'Grid', 1, 0, 0, '2025-06-26 17:40:00', NULL, '2025-06-30 09:13:57');
INSERT INTO `sys_menu` VALUES (2, '菜单管理', 1, 2, 0, 1, '/index/menu', 'system:menu:query', 1, 'Menu', 1, 0, 0, '2025-06-26 17:40:00', NULL, '2025-06-30 09:15:34');
INSERT INTO `sys_menu` VALUES (3, '添加菜单', 2, 3, 0, NULL, NULL, 'system:menu:add', NULL, NULL, 1, 0, 0, '2025-06-26 17:40:00', NULL, '2025-06-30 09:46:03');
INSERT INTO `sys_menu` VALUES (4, '编辑菜单', 2, 3, 1, NULL, NULL, 'system:menu:update', NULL, NULL, 1, 0, 1, '2025-06-30 09:02:59', NULL, '2025-06-30 09:46:06');
INSERT INTO `sys_menu` VALUES (6, '删除菜单', 2, 3, 2, NULL, NULL, 'system:menu:delete', NULL, NULL, 1, 0, 1, '2025-06-30 09:05:58', NULL, '2025-06-30 09:46:11');
INSERT INTO `sys_menu` VALUES (7, '角色管理', 1, 2, 1, 1, '/index/role', 'system:role:query', 1, 'UserFilled', 1, 0, 0, '2025-06-26 17:40:00', NULL, '2025-06-30 09:40:14');
INSERT INTO `sys_menu` VALUES (8, '添加角色', 7, 3, 0, NULL, NULL, 'system:role:add', NULL, NULL, 1, 0, 1, '2025-06-30 09:45:55', NULL, '2025-06-30 01:45:55');
INSERT INTO `sys_menu` VALUES (9, '编辑角色', 7, 3, 1, NULL, NULL, 'system:role:update', NULL, NULL, 1, 0, 1, '2025-06-30 09:52:06', NULL, '2025-06-30 01:52:06');
INSERT INTO `sys_menu` VALUES (10, '删除角色', 7, 3, 2, NULL, NULL, 'system:role:delete', NULL, NULL, 1, 0, 1, '2025-06-30 09:52:23', NULL, '2025-06-30 01:52:23');
INSERT INTO `sys_menu` VALUES (11, '组织管理', 1, 2, 2, 1, '/index/org', 'system:org:query', 1, 'Memo', 1, 0, 1, '2025-06-30 09:56:02', NULL, '2025-06-30 01:56:01');
INSERT INTO `sys_menu` VALUES (12, '添加组织', 11, 3, 0, NULL, NULL, 'system:org:add', NULL, NULL, 1, 0, 1, '2025-06-30 09:57:00', NULL, '2025-06-30 01:57:00');
INSERT INTO `sys_menu` VALUES (13, '编辑组织', 11, 3, 1, NULL, NULL, 'system:org:update', NULL, NULL, 1, 0, 1, '2025-06-30 01:57:42', 1, '2025-06-30 11:34:45');
INSERT INTO `sys_menu` VALUES (14, '组织授权', 11, 3, 2, NULL, NULL, 'system:org:authorize', NULL, NULL, 0, 0, 1, '2025-06-30 10:00:14', 1, '2025-06-30 10:07:56');
INSERT INTO `sys_menu` VALUES (15, '删除组织', 11, 3, 3, NULL, NULL, 'system:org:delete', NULL, NULL, 1, 0, 1, '2025-06-30 10:00:37', NULL, '2025-06-30 02:00:36');
INSERT INTO `sys_menu` VALUES (16, '岗位管理', 1, 2, 3, 1, '/index/post', 'system:post:query', 1, 'Postcard', 1, 0, 1, '2025-06-30 10:04:47', NULL, '2025-06-30 02:04:47');
INSERT INTO `sys_menu` VALUES (17, '添加岗位', 16, 3, 0, NULL, NULL, 'system:post:add', NULL, NULL, 1, 0, 1, '2025-06-30 10:05:36', NULL, '2025-06-30 02:05:35');
INSERT INTO `sys_menu` VALUES (18, '编辑岗位', 16, 3, 1, NULL, NULL, 'system:post:update', NULL, NULL, 1, 0, 1, '2025-06-30 10:05:50', 1, '2025-06-30 11:33:35');
INSERT INTO `sys_menu` VALUES (19, '岗位授权', 16, 3, 2, NULL, NULL, 'system:post:authorize', NULL, NULL, 0, 0, 1, '2025-06-30 10:06:15', 1, '2025-06-30 10:07:51');
INSERT INTO `sys_menu` VALUES (20, '岗位删除', 16, 3, 3, NULL, NULL, 'system:post:delete', NULL, NULL, 1, 0, 1, '2025-06-30 10:06:36', NULL, '2025-06-30 02:06:36');
INSERT INTO `sys_menu` VALUES (21, '用户管理', 1, 2, 4, 1, '/index/user', 'system:user:query', 1, 'User', 1, 0, 1, '2025-06-30 10:15:29', NULL, '2025-06-30 02:15:28');
INSERT INTO `sys_menu` VALUES (22, '添加用户', 21, 3, 0, NULL, NULL, 'system:user:add', NULL, NULL, 1, 0, 1, '2025-06-30 10:25:17', NULL, '2025-06-30 02:25:16');
INSERT INTO `sys_menu` VALUES (23, '编辑用户', 21, 3, 1, NULL, NULL, 'system:user:update', NULL, NULL, 1, 0, 1, '2025-06-30 10:25:35', NULL, '2025-06-30 02:25:34');
INSERT INTO `sys_menu` VALUES (24, '用户授权', 21, 3, 2, NULL, NULL, 'system:user:authorize', NULL, NULL, 1, 0, 1, '2025-06-30 10:25:48', 1, '2025-06-30 10:25:54');
INSERT INTO `sys_menu` VALUES (25, '删除用户', 21, 3, 3, NULL, NULL, 'system:user:delete', NULL, NULL, 1, 0, 1, '2025-06-30 10:26:36', NULL, '2025-06-30 02:26:36');
INSERT INTO `sys_menu` VALUES (26, '重置密钥', 21, 3, 5, NULL, NULL, 'system:user:secret:reset', NULL, NULL, 1, 0, 1, '2025-06-30 10:27:19', NULL, '2025-06-30 02:27:19');
INSERT INTO `sys_menu` VALUES (27, '字典管理', 1, 2, 5, 1, '/index/dict', 'system:dict:query', 1, 'Collection', 1, 0, 1, '2025-06-30 11:32:30', 1, '2025-06-30 11:32:39');
INSERT INTO `sys_menu` VALUES (28, '添加字典', 27, 3, 0, NULL, NULL, 'system:dict:add', NULL, NULL, 1, 0, 1, '2025-06-30 11:33:01', NULL, '2025-06-30 03:33:00');
INSERT INTO `sys_menu` VALUES (29, '编辑指点', 27, 3, 1, NULL, NULL, 'system:dict:update', NULL, NULL, 1, 0, 1, '2025-06-30 11:33:13', NULL, '2025-06-30 03:33:12');
INSERT INTO `sys_menu` VALUES (30, '删除字典', 27, 3, 2, NULL, NULL, 'system:dict:delete', NULL, NULL, 1, 0, 1, '2025-06-30 11:33:27', NULL, '2025-06-30 03:33:26');
INSERT INTO `sys_menu` VALUES (31, '系统参数', 1, 2, 6, 1, '/index/params', 'system:params:query', 1, 'Notebook', 1, 0, 1, '2025-06-30 16:56:47', NULL, '2025-06-30 08:56:47');
INSERT INTO `sys_menu` VALUES (32, '添加参数', 31, 3, 0, NULL, NULL, 'system:params:add', NULL, NULL, 1, 0, 1, '2025-06-30 16:57:05', NULL, '2025-06-30 08:57:04');
INSERT INTO `sys_menu` VALUES (33, '编辑参数', 31, 3, 1, NULL, NULL, 'system:params:update', NULL, NULL, 1, 0, 1, '2025-06-30 16:57:21', NULL, '2025-06-30 08:57:20');
INSERT INTO `sys_menu` VALUES (34, '删除参数', 31, 3, 2, NULL, NULL, 'system:params:delete', NULL, NULL, 1, 0, 1, '2025-06-30 16:57:34', NULL, '2025-06-30 08:57:34');
INSERT INTO `sys_menu` VALUES (35, '操作日志', 1, 2, 7, 1, '/index/optLog', 'system:log:opt:query', 1, 'DocumentCopy', 1, 0, 1, '2025-06-30 16:58:59', 1, '2025-07-01 14:58:40');
INSERT INTO `sys_menu` VALUES (36, '登录日志', 1, 2, 8, 1, '/index/loginLog', 'system:log:login:query', 1, 'Tickets', 1, 0, 1, '2025-06-30 17:03:08', 1, '2025-07-02 19:30:51');
INSERT INTO `sys_menu` VALUES (37, '商户管理', 0, 1, 100, NULL, NULL, NULL, 2, 'material-symbols:manage-accounts', 1, 0, 1, '2025-07-03 15:33:45', 1, '2025-07-03 15:34:11');

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`  (
  `operate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '操作ID',
  `module_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块名称',
  `operate_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型',
  `request_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `user_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求用户代理',
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作接口方法',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `ip` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作位置IP',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '基于IP地址的地理位置',
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作参数',
  `response_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '响应数据',
  `cost_time` bigint(20) NOT NULL COMMENT '耗时（毫秒）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作完成时间',
  PRIMARY KEY (`operate_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
  `org_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父部门ID',
  `org_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '部门状态 0-停用 1-启用/正常',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`org_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织结构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (1, 0, '集团', 0, NULL, NULL, 1, 0, 1, '2025-06-30 10:16:28', 1, '2025-07-02 19:35:11');
INSERT INTO `sys_org` VALUES (2, 1, '总办', 0, NULL, NULL, 1, 0, 1, '2025-06-30 10:22:58', 1, '2025-07-02 19:52:09');
INSERT INTO `sys_org` VALUES (3, 1, '人事部', 1, NULL, NULL, 1, 0, 1, '2025-06-30 10:23:07', NULL, '2025-06-30 02:23:06');
INSERT INTO `sys_org` VALUES (4, 1, '财务部', 2, NULL, NULL, 1, 0, 1, '2025-06-30 10:23:25', NULL, '2025-06-30 02:23:25');
INSERT INTO `sys_org` VALUES (5, 1, '行政部', 4, NULL, NULL, 1, 0, 1, '2025-06-30 10:23:36', NULL, '2025-06-30 02:23:36');
INSERT INTO `sys_org` VALUES (6, 1, '互联网BU', 5, NULL, NULL, 1, 0, 1, '2025-06-30 10:24:01', NULL, '2025-06-30 02:24:00');
INSERT INTO `sys_org` VALUES (7, 1, '海外事业部', 6, NULL, NULL, 1, 0, 1, '2025-06-30 10:24:34', NULL, '2025-06-30 02:24:34');
INSERT INTO `sys_org` VALUES (8, 1, '公共事业部', 8, NULL, NULL, 1, 0, 1, '2025-06-30 18:34:57', NULL, '2025-06-30 10:34:57');

-- ----------------------------
-- Table structure for sys_org_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_org_role`;
CREATE TABLE `sys_org_role`  (
  `org_id` bigint(20) NOT NULL COMMENT '部门ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`org_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_params
-- ----------------------------
DROP TABLE IF EXISTS `sys_params`;
CREATE TABLE `sys_params`  (
  `param_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，自增标识符',
  `param_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数代码',
  `param_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名',
  `param_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数值',
  `param_desc` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数描述',
  `data_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数的数据类型(boolean/int/string/json)',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`param_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_params
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0-停用 1-启用/正常',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'CEO', '董事长', 0, 1, NULL, 0, 1, '2025-06-30 10:28:09', 1, '2025-07-03 09:14:57');

-- ----------------------------
-- Table structure for sys_post_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_post_role`;
CREATE TABLE `sys_post_role`  (
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`post_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '岗位角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_perms` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码/权限编码',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '部门状态 0-停用 1-启用/正常',
  `deleted` int(11) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ADMIN', 0, '超级管理员角色', 1, 0, 0, '2025-06-26 17:40:00', 1, '2025-07-01 11:28:20');
INSERT INTO `sys_role` VALUES (2, '审计人员', 'AUDITOR', 1, NULL, 1, 0, 1, '2025-06-30 17:07:21', 1, '2025-06-30 17:07:32');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1, '2025-06-30 09:07:21');
INSERT INTO `sys_role_menu` VALUES (2, 35, '2025-06-30 09:07:21');
INSERT INTO `sys_role_menu` VALUES (2, 36, '2025-06-30 09:07:21');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `enable_mfa` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否启用MFA 0-未启用 1-启用',
  `google_otp_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'google otp 密钥',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态 0-停用 1-启用/正常',
  `deleted` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标志 0-未删除 1-已删除',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
-- admin/admin123  jonlu/jonlu123
INSERT INTO `sys_user` VALUES (1, 'admin', 'Admin', 'lujiafayx@163.com', '18118778881', NULL, '$2a$10$4D0R612DwEYlLPmlQ0UxmuJD7uQbek7fH39KD9ZKr2rkrGCr031kW', 0, 'SYOAZUMOQG6ZHK5F', 1, 0, 0, '2025-06-26 17:27:00', 1, '2025-07-02 16:51:39');
INSERT INTO `sys_user` VALUES (2, 'jonlu', 'Jon', 'lujiafayx@163.com', '18118778841', NULL, '$2a$10$ToAt1rU008Ls3xW6CG/SGOa3DvB3aUP7RXDodupCSFgr1krfbk9BK', 0, 'S6LQWETTIJZTKMFK', 1, 0, 1, '2025-06-30 17:05:35', 1, '2025-06-30 17:10:08');

-- ----------------------------
-- Table structure for sys_user_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_org`;
CREATE TABLE `sys_user_org`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `org_id` bigint(20) NOT NULL COMMENT '部门ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`, `org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_org
-- ----------------------------
INSERT INTO `sys_user_org` VALUES (1, 1, '2025-06-30 08:47:58');
INSERT INTO `sys_user_org` VALUES (2, 2, '2025-06-30 09:05:34');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (2, 1, '2025-06-30 09:05:34');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, '2025-06-30 08:48:16');
INSERT INTO `sys_user_role` VALUES (2, 2, '2025-06-30 09:09:08');

