CREATE TABLE `isv_corp_staff` (
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_user` (`corp_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='isv存储的公司成员信息';

ALTER TABLE `isv_corp_staff`
  ADD COLUMN `rsq_user_id`  varchar(255) NULL COMMENT '日事清应用用户id',
  ADD COLUMN `rsq_username`  varchar(255) NULL COMMENT '日事清应用登录用户名id',
  ADD COLUMN `rsq_password`  varchar(255) NULL COMMENT '日事清应用登录用户密码';

ALTER TABLE `isv_corp`
  ADD COLUMN `rsq_id`  varchar(255) NULL COMMENT '日事清应用公司id';

ALTER TABLE `isv_suite`
  ADD COLUMN `rsq_app_name`  varchar(255) NULL COMMENT '日事清应用中显示的本app的名称',
  ADD COLUMN `rsq_app_token`  varchar(255) NULL COMMENT '日事清应用授权的token，用来访问日事清token授权接口';

# 为防止冲突，不使用钉钉原来的isv_biz_lock，而使用如下isv_con_lock
CREATE TABLE `isv_con_lock` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `lock_key` varchar(256) NOT NULL COMMENT '锁key',
  `expire` datetime DEFAULT NULL COMMENT '过期时间,null表示不过期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_lock_key` (`lock_key`(191))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='db锁';

