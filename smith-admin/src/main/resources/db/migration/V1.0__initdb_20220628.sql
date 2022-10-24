CREATE TABLE IF NOT EXISTS `t_data_source`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`        tinyint(4)  NOT NULL DEFAULT '1' COMMENT '数据源类型，1: MySQL 2: http 3: ClickHouse',
    `name`        varchar(50) NOT NULL COMMENT '名称',
    `url`         varchar(200)         DEFAULT NULL COMMENT '链接地址',
    `host`        varchar(64)          DEFAULT NULL COMMENT '主机',
    `port`        smallint(8)          DEFAULT '0' COMMENT '端口',
    `account`     varchar(50)          DEFAULT NULL COMMENT '账号',
    `password`    varchar(256)         DEFAULT NULL COMMENT '密码',
    `salt`        varchar(50)          DEFAULT NULL COMMENT '加密盐值',
    `headers`     varchar(200)         DEFAULT NULL COMMENT '请求头',
    `status`      tinyint(4)  NOT NULL COMMENT '状态, 1正常, 9删除',
    `remark`      varchar(50)          DEFAULT NULL COMMENT '备注',
    `create_time` bigint(20)  NOT NULL COMMENT '创建时间',
    `update_time` bigint(20)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='数据源';

CREATE TABLE `t_data_model`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        varchar(100) NOT NULL COMMENT '名称',
    `source_id`   int(11)      NOT NULL DEFAULT '0' COMMENT '数据源ID',
    `content`     text         NOT NULL COMMENT '执行内容，SQL语句/接口地址',
    `parameter`   varchar(500)          DEFAULT NULL COMMENT '执行参数',
    `json_path`   varchar(50)           DEFAULT NULL COMMENT '数据解析JSON路径',
    `field_data`  json                  DEFAULT NULL COMMENT '字段数据 {key: name}',
    `create_time` bigint(20)   NOT NULL COMMENT '创建时间',
    `update_time` bigint(20)   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT ='数据模型';

CREATE TABLE `t_data_table`
(
    `id`            bigint(20)  NOT NULL COMMENT '主键',
    `table_name`    varchar(50) NOT NULL COMMENT '名称',
    `data_model_id` int(11)     NOT NULL COMMENT '数据模型ID',
    `app_id`        varchar(50) NOT NULL COMMENT '外部应用ID',
    `app_name`      varchar(50)          DEFAULT NULL COMMENT '外部应用名称',
    `app_type`      tinyint(4)  NOT NULL DEFAULT '1' COMMENT '外部应用类型,1飞书多维表格',
    `app_table_id`  varchar(20)          DEFAULT NULL COMMENT '外部业务表ID',
    `sync_status`   tinyint(4)  NOT NULL DEFAULT '0' COMMENT '同步状态，0未同步，1已同步, 2同步失败',
    `sync_cause`    varchar(200) DEFAULT NULL COMMENT '同步失败原因',
    `status`        tinyint(4)  NOT NULL DEFAULT '1' COMMENT '1正常, 9删除',
    `cron`          varchar(20)          DEFAULT NULL COMMENT 'cron表达式',
    `remark`        varchar(50)          DEFAULT NULL COMMENT '备注',
    `execute_time`  bigint(20)           DEFAULT NULL COMMENT '数据同步执行时间',
    `create_time`   bigint(20)  NOT NULL COMMENT '创建时间',
    `update_time`   bigint(20)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_bi_table_id` (`table_name`, `app_id`)
) COMMENT ='数据表格定义';

CREATE TABLE IF NOT EXISTS `t_data_field`
(
    `id`           bigint(20)   NOT NULL COMMENT '主键',
    `table_id`     bigint(20)   NOT NULL COMMENT '数据表格ID',
    `field_key`    varchar(25)  NOT NULL COMMENT '字段键值',
    `field_name`   varchar(50)  NOT NULL COMMENT '名称',
    `primary_flag` bit(1)       NOT NULL DEFAULT b'0' COMMENT '主键标识',
    `type`         tinyint(4)   NOT NULL COMMENT '类型，1：多行文本 2：数字 3：单选 4：多选 5：日期',
    `app_field_id` varchar(20)           DEFAULT NULL COMMENT '外部应用字段ID',
    `property`     varchar(250) NOT NULL COMMENT '字段属性',
    `sync_status`  tinyint(4)   NOT NULL DEFAULT '0' COMMENT '同步状态，0未同步，1已同步, 2同步失败',
    `sync_cause`   varchar(200) DEFAULT NULL COMMENT '同步失败原因',
    `status`       tinyint(4)   NOT NULL DEFAULT '1' COMMENT '1正常, 9删除',
    `sort_no`      int(11)      NOT NULL DEFAULT '0' COMMENT '排序号',
    `create_time`  bigint(20)   NOT NULL COMMENT '创建时间',
    `update_time`  bigint(20)   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_bi_table_id` (`field_name`, `table_id`)
) COMMENT ='数据表格字段';

CREATE TABLE IF NOT EXISTS `t_job_log`
(
    `id`           bigint(20) NOT NULL COMMENT '主键',
    `handler_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '处理者ID',
    `handler_name` varchar(50)         DEFAULT NULL COMMENT '处理者名称',
    `insert_num`   int(11)    NOT NULL DEFAULT '0' COMMENT '新增数量',
    `update_num`   int(11)    NOT NULL DEFAULT '0' COMMENT '修改数量',
    `delete_num`   int(11)    NOT NULL DEFAULT '0' COMMENT '删除数量',
    `trigger_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '触发类型，1手动触发，2自动触发',
    `result`       tinyint(4) NOT NULL COMMENT '执行结果，1成功，2失败',
    `cost_time`    int(11)    NOT NULL DEFAULT '0' COMMENT '耗时(ms)',
    `cause`        text COMMENT '异常原因',
    `create_time`  bigint(20) NOT NULL COMMENT '创建时间',
    `update_time`  bigint(20) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_job_id` (`handler_id`)
) COMMENT ='任务作业日志';

CREATE TABLE IF NOT EXISTS `t_sys_user`
(
    `id`          bigint(20)  NOT NULL COMMENT '主键',
    `name`        varchar(20) NOT NULL COMMENT '用户名',
    `account`     varchar(20) NOT NULL COMMENT '账号名',
    `password`    varchar(50)          DEFAULT NULL COMMENT '密码',
    `status`      tinyint(4)  NOT NULL COMMENT '状态，1正常，9删除',
    `login_time`  bigint(20)  NOT NULL DEFAULT '0' COMMENT '上次登录时间',
    `create_time` bigint(20)  NOT NULL COMMENT '创建时间',
    `update_time` bigint(20)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_account` (`account`)
) COMMENT ='系统用户';