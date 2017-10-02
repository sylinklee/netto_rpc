CREATE TABLE schedule_tablefix (
  `r_id`      INT(11)      NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `task_type` VARCHAR(255) NOT NULL
  COMMENT '任务类型',
  `table_fix` TINYINT(4)   NOT NULL
  COMMENT '任务表后缀',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_tablefix_tasktype` (`task_type`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务路由表';

CREATE TABLE schedule_task_0 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_0_fingerprint` (`fingerprint`),
  KEY `schedule_task_0_index1` (`task_key1`),
  KEY `schedule_task_0_index2`(`task_key2`),
  KEY `schedule_task_0_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_0';

CREATE TABLE schedule_task_1 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_1_fingerprint` (`fingerprint`),
  KEY `schedule_task_1_index1` (`task_key1`),
  KEY `schedule_task_1_index2`(`task_key2`),
  KEY `schedule_task_1_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_1';

CREATE TABLE schedule_task_2 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_2_fingerprint` (`fingerprint`),
  KEY `schedule_task_2_index1` (`task_key1`),
  KEY `schedule_task_2_index2`(`task_key2`),
  KEY `schedule_task_2_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_2';

CREATE TABLE schedule_task_3 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_3_fingerprint` (`fingerprint`),
  KEY `schedule_task_3_index1` (`task_key1`),
  KEY `schedule_task_3_index2`(`task_key2`),
  KEY `schedule_task_3_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_3';

CREATE TABLE schedule_task_4 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_4_fingerprint` (`fingerprint`),
  KEY `schedule_task_4_index1` (`task_key1`),
  KEY `schedule_task_4_index2`(`task_key2`),
  KEY `schedule_task_4_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_4';

CREATE TABLE schedule_task_5 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_5_fingerprint` (`fingerprint`),
  KEY `schedule_task_5_index1` (`task_key1`),
  KEY `schedule_task_5_index2`(`task_key2`),
  KEY `schedule_task_5_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_5';

CREATE TABLE schedule_task_6 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_6_fingerprint` (`fingerprint`),
  KEY `schedule_task_6_index1` (`task_key1`),
  KEY `schedule_task_6_index2`(`task_key2`),
  KEY `schedule_task_6_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_6';

CREATE TABLE schedule_task_7 (
  `r_id`          INT(11)       NOT NULL  AUTO_INCREMENT
  COMMENT '主键',
  `create_time`   DATETIME      NOT NULL
  COMMENT '创建时间',
  `task_type`     VARCHAR(255)  NOT NULL
  COMMENT '任务类型',
  `task_key1`     VARCHAR(100)  NULL
  COMMENT '任务关键字1',
  `task_key2`     VARCHAR(100)  NULL
  COMMENT '任务关键字2',
  `body_class`    VARCHAR(255)  NOT NULL
  COMMENT '任务体类名',
  `task_body`     VARCHAR(4000) NOT NULL
  COMMENT '任务体内容',
  `owner` VARCHAR(128) NULL
  COMMENT '任务来源',  
  `status`        TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '状态（‘-1’取消‘0’初始化 ‘1’执行中 ‘2’完成 ‘10’失败）',
  `execute_count` INT(11)       NOT NULL  DEFAULT '0'
  COMMENT '执行次数',
  `last_time`     DATETIME      NOT NULL
  COMMENT '最近更新时间',
  `region_no`     TINYINT(4)    NOT NULL  DEFAULT '0'
  COMMENT '任务分区号',
  `remark`        VARCHAR(255)  NULL
  COMMENT '备注',
  `fingerprint`   VARCHAR(255)  NOT NULL
  COMMENT '任务指纹',
  PRIMARY KEY (`r_id`),
  UNIQUE KEY `schedule_task_7_fingerprint` (`fingerprint`),
  KEY `schedule_task_7_index1` (`task_key1`),
  KEY `schedule_task_7_index2`(`task_key2`),
  KEY `schedule_task_7_select` (`status`, `region_no`)
)
  ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  COMMENT = '任务表_7';