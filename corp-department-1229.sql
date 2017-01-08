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

CREATE TABLE `isv_corp_org_sync_fail` (
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

CREATE TABLE `isv_corp_staff_deleted` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(128) NOT NULL COMMENT '公司id',
  `user_id` varchar(128) NOT NULL COMMENT '用户id，此id跟是指在corp内部的id',
  `name` varchar(128) COMMENT '成员名称',
  `tel` varchar(128) COMMENT '分机号（ISV不可见）',
  `work_place` varchar(128) COMMENT '办公地点（ISV不可见）',
  `remark` varchar(128) COMMENT '备注（ISV不可见）',
  `mobile` varchar(128) COMMENT '手机号码（ISV不可见）',
  `email` varchar(128) COMMENT '员工的电子邮箱（ISV不可见）',
  `active` varchar(1) COMMENT '是否已经激活, true表示已激活, false表示未激活',
  `order_in_depts` varchar(1024) COMMENT '在对应的部门中的排序, Map结构的json字符串, key是部门的Id, value是人员在这个部门的排序值',
  `is_admin` varchar(1) COMMENT '是否为企业的管理员, true表示是, false表示不是',
  `is_boss` varchar(1) COMMENT '是否为企业的老板, true表示是, false表示不是',
  `ding_id` varchar(128) COMMENT '钉钉Id',
  `is_leader_in_depts` varchar(1024) COMMENT '在对应的部门中是否为主管, Map结构的json字符串, key是部门的Id, value是人员在这个部门中是否为主管, true表示是, false表示不是',
  `is_hide` varchar(1) COMMENT '是否号码隐藏, true表示隐藏, false表示不隐藏',
  `department` varchar(1024) COMMENT '成员所属部门id列表',
  `position` varchar(128) COMMENT '职位信息',
  `avatar` varchar(256) COMMENT '头像url',
  `jobnumber` varchar(128) COMMENT '员工工号',
  `extattr` varchar(1024) COMMENT '扩展属性，可以设置多种属性(但手机上最多只能显示10个扩展属性，具体显示哪些属性，请到OA管理后台->设置->通讯录信息设置和OA管理后台->设置->手机端显示信息设置)性',
  `is_sys` varchar(1) COMMENT '是否是管理员，code免登时获取到',
  `sys_level` bigint(10) COMMENT '级别，0：非管理员 1：超级管理员（主管理员） 2：普通管理员（子管理员） 100：老板',
  `rsq_user_id`  varchar(255) NULL COMMENT '日事清应用用户id',
  `rsq_username`  varchar(255) NULL COMMENT '日事清应用登录用户名id',
  `rsq_password`  varchar(255) NULL COMMENT '日事清应用登录用户密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_user` (`corp_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='公司成员被删除后移入的表';

ALTER TABLE `isv_corp_staff_deleted`
  ADD COLUMN `union_id`  varchar(255) NULL COMMENT '钉钉union id';




