CREATE TABLE `task` (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `beanName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '任务名称',
      `methodName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '方法名称',
      `methodParams` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '方法参数',
      `cronExpression` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'cron表达式',
      `jobStatus` char(1) COLLATE utf8_unicode_ci DEFAULT '0' COMMENT '任务状态 0暂停 1正常',
      `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
      `createTime` date DEFAULT NULL COMMENT '创建时间',
      `updateTime` date DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `beanName` (`beanName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='定时任务表';

