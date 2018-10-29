CREATE TABLE `open_sync_biz_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `subscribe_id` varchar(64) NOT NULL COMMENT '订阅方ID',
  `corp_id` varchar(64) NOT NULL COMMENT '企业ID',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `biz_type` int(11) NOT NULL COMMENT '业务类型',
  `biz_data` text NOT NULL COMMENT '业务数据',
  `open_cursor` bigint(20) NOT NULL COMMENT '对账游标',
  `status` int(11) NOT NULL COMMENT '处理状态0为未处理。其他状态开发者自行定义',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subscribe_corp_biz` (`subscribe_id`,`corp_id`,`biz_id`,`biz_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='高优先级数据';

CREATE TABLE `open_sync_biz_data_medium` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `subscribe_id` varchar(64) NOT NULL COMMENT '订阅方ID',
  `corp_id` varchar(64) NOT NULL COMMENT '企业ID',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `biz_type` int(11) NOT NULL COMMENT '业务类型',
  `biz_data` text NOT NULL COMMENT '业务数据',
  `open_cursor` bigint(20) NOT NULL COMMENT '对账游标',
  `status` int(11) NOT NULL COMMENT '处理状态0为未处理。其他状态开发者自行定义',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subscribe_corp_biz` (`subscribe_id`,`corp_id`,`biz_id`,`biz_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='中低优先级数据';

CREATE TABLE `open_global_lock`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `lock_key` varchar(32) NULL,
  `status` varchar(32) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_global_lock` (`lock_key`) USING BTREE
);