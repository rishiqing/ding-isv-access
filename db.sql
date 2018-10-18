CREATE DATABASE IF NOT EXISTS ding_isv_access DEFAULT CHARSET utf8mb4 COLLATE utf8_general_ci;

CREATE TABLE `isv_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pl',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(128) NOT NULL COMMENT '微应用套件key',
  `app_id` bigint(20) NOT NULL COMMENT 'appid,此id来自于开发者中心',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_app` (`suite_key`,`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='isv创建的app';

CREATE TABLE `isv_biz_lock` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `lock_key` varchar(256) NOT NULL COMMENT '锁key',
  `expire` datetime DEFAULT NULL COMMENT '过期时间,null表示不过期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_lock_key` (`lock_key`(191))
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='db锁';

CREATE TABLE `isv_corp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(128) NOT NULL COMMENT '钉钉企业ID',
  `invite_code` varchar(64) DEFAULT NULL COMMENT '企业邀请码',
  `industry` varchar(256) DEFAULT NULL COMMENT '企业所属行业',
  `corp_name` varchar(256) DEFAULT NULL COMMENT '企业名称',
  `invite_url` varchar(1024) DEFAULT NULL COMMENT '企业邀请链接',
  `corp_logo_url` varchar(1024) DEFAULT NULL COMMENT '企业logo链接',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_id` (`corp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COMMENT='企业信息表';


CREATE TABLE `isv_corp_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `agent_id` bigint(20) NOT NULL COMMENT '钉钉企业使用的微应用ID',
  `agent_name` varchar(128) NOT NULL COMMENT '钉钉企业使用的微应用名称',
  `logo_url` varchar(1024) DEFAULT NULL COMMENT '钉钉企业使用的微应用图标',
  `app_id` bigint(20) NOT NULL COMMENT '钉钉企业使用的微应用原始ID',
  `corp_id` varchar(128) NOT NULL COMMENT '使用微应用的企业ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_app` (`corp_id`,`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COMMENT='企业微应用信息表';


CREATE TABLE `isv_corp_suite_auth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(255) NOT NULL COMMENT '企业corpid',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `permanent_code` varchar(255) NOT NULL COMMENT '临时授权码或永久授权码value',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_suite` (`corp_id`(191),`suite_key`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COMMENT='企业对套件的授权记录';


CREATE TABLE `isv_corp_suite_auth_faile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '企业id',
  `faile_info` varchar(256) DEFAULT NULL COMMENT '失败信息',
  `auth_faile_type` varchar(128) NOT NULL COMMENT '授权失败类型',
  `suite_push_type` varchar(128) NOT NULL COMMENT '推送类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_c_s_f_p` (`suite_key`,`corp_id`,`auth_faile_type`,`suite_push_type`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COMMENT='企业对套件的授权失败记录';


CREATE TABLE `isv_corp_suite_callback` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(128) NOT NULL COMMENT '企业corpid',
  `suite_key` varchar(128) NOT NULL COMMENT '套件key',
  `callback_tag` varchar(1024) NOT NULL COMMENT '注册事件tag,json结构存储',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业注册回调事件';


CREATE TABLE `isv_corp_suite_jsapi_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '钉钉企业id',
  `corp_jsapi_ticket` varchar(256) NOT NULL COMMENT '企业js_ticket',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_corp` (`suite_key`,`corp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12206 DEFAULT CHARSET=utf8mb4 COMMENT='企业使用jsapi的js ticket表';


CREATE TABLE `isv_corp_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '钉钉企业id',
  `corp_token` varchar(256) NOT NULL COMMENT '企业token',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_corp` (`suite_key`,`corp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1823 DEFAULT CHARSET=utf8mb4 COMMENT='套件能够访问企业数据的accesstoken';


CREATE TABLE `isv_suite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_name` varchar(255) NOT NULL COMMENT '套件名字',
  `suite_key` varchar(100) NOT NULL COMMENT 'suite 的唯一key',
  `suite_secret` varchar(256) NOT NULL COMMENT 'suite的唯一secrect，与key对应',
  `encoding_aes_key` varchar(256) NOT NULL COMMENT '回调信息加解密参数',
  `token` varchar(128) NOT NULL COMMENT '已填写用于生成签名和校验毁掉请求的合法性',
  `event_receive_url` varchar(256) NOT NULL COMMENT '回调地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_key` (`suite_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='套件信息表';


CREATE TABLE `isv_suite_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件suitekey',
  `ticket` varchar(100) NOT NULL COMMENT '套件ticket',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_key` (`suite_key`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='用于接收推送的套件ticket';


CREATE TABLE `isv_suite_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `suite_token` varchar(256) NOT NULL COMMENT '套件token',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_key` (`suite_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='套件的accesstoken表';



CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `CALENDAR_NAME` varchar(64) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `JOB_NAME` varchar(64) NOT NULL,
  `JOB_GROUP` varchar(64) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `JOB_NAME` varchar(64) NOT NULL,
  `JOB_GROUP` varchar(64) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(64) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `INSTANCE_NAME` varchar(64) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(64) DEFAULT NULL,
  `JOB_GROUP` varchar(64) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `INSTANCE_NAME` varchar(64) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(64) NOT NULL,
  `TRIGGER_NAME` varchar(64) NOT NULL,
  `TRIGGER_GROUP` varchar(64) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# add-10.11.sql
CREATE TABLE `isv_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pl',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(128) NOT NULL COMMENT '微应用套件key',
  `app_id` bigint(20) NOT NULL COMMENT '服务窗appid,此id来自于开发者中心',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_app` (`suite_key`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='isv创建的服务窗app';


CREATE TABLE `isv_corp_suite_jsapi_channel_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '套件key',
  `corp_id` varchar(100) NOT NULL COMMENT '钉钉企业id',
  `corp_channel_jsapi_ticket` varchar(256) NOT NULL COMMENT '企业服务窗js_ticket',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_corp` (`suite_key`,`corp_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='企业使用服务窗jsapi的js ticket表';


CREATE TABLE `isv_corp_channel_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `agent_id` bigint(20) NOT NULL COMMENT '钉钉企业使用的服务窗应用ID',
  `agent_name` varchar(128) NOT NULL COMMENT '钉钉企业使用的服务窗应用名称',
  `logo_url` varchar(1024) DEFAULT NULL COMMENT '钉钉企业使用的服务窗应用图标',
  `app_id` bigint(20) NOT NULL COMMENT '钉钉企业使用的服务窗应用原始ID',
  `corp_id` varchar(128) NOT NULL COMMENT '使用微应用的企业ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_corp_app` (`corp_id`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COMMENT='企业服务窗应用信息表';

CREATE TABLE `isv_corp_channel_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `suite_key` varchar(100) NOT NULL COMMENT '服务窗应用套件key',
  `corp_id` varchar(128) NOT NULL COMMENT '使用微应用的企业ID',
  `corp_channel_token` varchar(256) NOT NULL COMMENT '服务窗应用的token',
  `expired_time` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_suite_corp_channel` (`corp_id`,`suite_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COMMENT='企业服务窗应用token信息表';

ALTER TABLE `isv_corp_suite_auth`
  MODIFY COLUMN `permanent_code`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '临时授权码或永久授权码value' AFTER `suite_key`,
  ADD COLUMN `ch_permanent_code`  varchar(255) NULL COMMENT '企业服务窗永久授权码' AFTER `permanent_code`;


# corp-staff-1119.sql
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



# corp-department-1229.sql
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

# add 2018-09-29
ALTER TABLE `isv_corp_staff`
  ADD COLUMN `rsq_login_token`  varchar(255) NULL COMMENT '日事清的登录标识';

# add 2018-10-18
CREATE TABLE `isv_order_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `event_type` varchar(100) NOT NULL COMMENT 'market_buy',
  `suite_key` varchar(100) NOT NULL COMMENT '用户购买套件的SuiteKey',
  `buy_corp_id` varchar(128) NOT NULL COMMENT '购买该套件企业的corpid',
  `goods_code` varchar(256) NOT NULL COMMENT '购买的商品码',
  `item_code` varchar(256) NOT NULL COMMENT '购买的商品规格码',
  `item_name` varchar(256) NOT NULL COMMENT '购买的商品规格名称',
  `sub_quantity` bigint(10) COMMENT '订购的具体人数',
  `max_of_people` bigint(10) COMMENT '购买的商品规格能服务的最多企业人数',
  `min_of_people` bigint(10) COMMENT '购买的商品规格能服务的最少企业人数',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `paidtime` bigint(20) NOT NULL COMMENT '下单时间',
  `service_stop_time` bigint(20) NOT NULL COMMENT '该订单的服务到期时间',
  `pay_fee` bigint(10) NOT NULL COMMENT '订单支付费用,以分为单位',
  `order_create_source` varchar(256) COMMENT '订单创建来源，如果来自钉钉分销系统，则值为“DRP”',
  `nominal_pay_fee` bigint(10) COMMENT '钉钉分销系统提单价，以分为单位',
  `discount_fee` bigint(10) COMMENT '折扣减免费用',
  `discount` decimal(10,6) COMMENT '订单折扣',
  `distributor_corp_id` varchar(256) COMMENT '钉钉分销系统提单的代理商的企业corpId',
  `distributor_corp_name` varchar(256) COMMENT '钉钉分销系统提单的代理商的企业名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='isv接收到的订单事件记录';

CREATE TABLE `isv_order_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `suite_key` varchar(100) NOT NULL COMMENT '用户购买套件的SuiteKey',
  `buy_corp_id` varchar(128) NOT NULL COMMENT '购买该套件企业的corpid',
  `goods_code` varchar(256) DEFAULT NULL COMMENT '购买的商品码',
  `item_code` varchar(256) NOT NULL COMMENT '购买的商品规格码',
  `item_name` varchar(256) NOT NULL COMMENT '购买的商品规格名称',
  `sub_quantity` bigint(10) COMMENT '订购的具体人数',
  `max_of_people` bigint(10) COMMENT '购买的商品规格能服务的最多企业人数',
  `min_of_people` bigint(10) COMMENT '购买的商品规格能服务的最少企业人数',
  `paidtime` bigint(20) NOT NULL COMMENT '下单时间',
  `service_stop_time` bigint(20) NOT NULL COMMENT '该订单的服务到期时间',
  `pay_fee` bigint(10) NOT NULL COMMENT '订单支付费用,以分为单位',
  `order_create_source` varchar(256) COMMENT '订单创建来源，如果来自钉钉分销系统，则值为“DRP”',
  `nominal_pay_fee` bigint(10) COMMENT '钉钉分销系统提单价，以分为单位',
  `discount_fee` bigint(10) COMMENT '折扣减免费用',
  `discount` decimal(10,6) COMMENT '订单折扣',
  `distributor_corp_id` varchar(256) COMMENT '钉钉分销系统提单的代理商的企业corpId',
  `distributor_corp_name` varchar(256) COMMENT '钉钉分销系统提单的代理商的企业名称',
  `status` varchar(32) COMMENT '订单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='订单状态表';

CREATE TABLE `isv_order_spec_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `goods_code` varchar(256) NOT NULL COMMENT '购买的商品码',
  `item_code` varchar(256) NOT NULL COMMENT '购买的商品规格码',
  `item_name` varchar(256) NOT NULL COMMENT '购买的商品规格名称',
  `inner_key` varchar(32) NOT NULL COMMENT '内部key值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

CREATE TABLE `isv_order_rsq_push_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `corp_id` varchar(128) NOT NULL COMMENT '购买该套件企业的corpid',
  `quantity` bigint(10) NOT NULL COMMENT '订购的具体人数',
  `service_stop_time` bigint(20) NOT NULL COMMENT '该订单的服务到期时间',
  `status` varchar(32) NOT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='发送到日事清的事件表';

CREATE TABLE `isv_corp_charge_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `corp_id` varchar(128) NOT NULL COMMENT '购买该套件企业的corpid',
  `total_quantity` bigint(10) COMMENT '公司总人数',
  `current_order_id` bigint(20) COMMENT '订单id',
  `current_sub_quantity` bigint(10) COMMENT '订购的具体人数',
  `current_max_of_people` bigint(10) COMMENT '当前规格的最大人数',
  `current_min_of_people` bigint(10) COMMENT '当前规格的最小人数',
  `current_service_stop_time` bigint(20) COMMENT '当前订单的服务到期时间',
  `status` varchar(32) NOT NULL COMMENT '公司状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='公司充值状态表';

CREATE TABLE `isv_staff_popup_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `popup_type` varchar(128) NOT NULL COMMENT '弹窗类型',
  `sale_qr_code_url` varchar(256) COMMENT '弹窗的二维码链接',
  `sale_phone_number` varchar(64) COMMENT '弹窗的手机号',
  `is_disabled` varchar(1)DEFAULT 0 COMMENT '是否禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='弹窗配置表';

CREATE TABLE `staff_popup_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `popup_type` varchar(128) NOT NULL COMMENT '弹窗类型',
  `corp_id` varchar(256) NOT NULL COMMENT '公司id',
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `popup_mute_expire` bigint(20) COMMENT '沉默期截至时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='弹窗记录表';