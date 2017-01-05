ALTER TABLE `isv_corp_staff`
  ADD COLUMN `union_id`  varchar(255) NULL COMMENT '钉钉union id';

CREATE TABLE `isv_corp_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(128) NOT NULL COMMENT '公司id',
  `dept_id` bigint(10) NOT NULL COMMENT '部门id',
  `name` varchar(128) COMMENT '部门名称',
  `order` bigint(10) COMMENT '在父部门中的次序值',
  `parent_id` bigint(10) COMMENT '父部门的dept_id',
  `create_dept_group` varchar(1) COMMENT '是否同步创建一个关联此部门的企业群, true表示是, false表示不是',
  `auto_add_use` varchar(1) COMMENT '当群已经创建后，是否有新人加入部门会自动加入该群, true表示是, false表示不是',
  `dept_hiding` varchar(1) COMMENT '是否隐藏部门, true表示隐藏, false表示显示',
  `dept_perimits` varchar(1024) COMMENT '可以查看指定隐藏部门的其他部门列表，如果部门隐藏，则此值生效，取值为其他的部门id组成的的字符串，使用|符号进行分割',
  `user_perimits` varchar(1024) COMMENT '可以查看指定隐藏部门的其他人员列表，如果部门隐藏，则此值生效，取值为其他的人员userid组成的的字符串，使用|符号进行分割',
  `outer_dept` varchar(1) COMMENT '是否本部门的员工仅可见员工自己, 为true时，本部门员工默认只能看到员工自己',
  `outer_permit_depts` varchar(1024) COMMENT '本部门的员工仅可见员工自己为true时，可以配置额外可见部门，值为部门id组成的的字符串，使用|符号进行分割',
  `outer_permit_users` varchar(1024) COMMENT '本部门的员工仅可见员工自己为true时，可以配置额外可见人员，值为userid组成的的字符串，使用| 符号进行分割',
  `org_dept_owner` varchar(128) COMMENT '企业群群主',
  `dept_manager_userid_list` varchar(1024) COMMENT '部门的主管列表,取值为由主管的userid组成的字符串，不同的userid使用|符号进行分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_user` (`corp_id`,`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='isv存储的公司部门信息';

ALTER TABLE `isv_corp_dept`
  ADD COLUMN `rsq_id`  varchar(255) NULL COMMENT '钉钉与日事清绑定的id';

CREATE TABLE `isv_corp_org_fetch_fail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '企业id',
  `fail_info` varchar(256) DEFAULT NULL COMMENT '失败信息',
  `corp_fail_type` varchar(128) NOT NULL COMMENT '企业失败类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_c_s_f_p` (`suite_key`,`corp_id`,`corp_fail_type`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='企业同步组织结构信息失败记录';

CREATE TABLE `isv_corp_callback_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '企业id',
  `is_success` varchar(1) DEFAULT 0 COMMENT '是否成功执行',
  `tag` varchar(100) NOT NULL COMMENT '回调标记',
  `fail_info` varchar(256) DEFAULT NULL COMMENT '错误信息',
  `event_json` varchar(512) NOT NULL COMMENT '企业回调事件的JSON',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='企业通讯录事件回调的记录';

CREATE TABLE `isv_corp_callback_fail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '企业id',
  `tag` varchar(100) NOT NULL COMMENT '回调标记',
  `fail_info` varchar(256) DEFAULT NULL COMMENT '错误信息',
  `event_json` varchar(512) NOT NULL COMMENT '企业回调事件的JSON',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='企业通讯录事件回调失败的记录';



