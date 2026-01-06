-- 校园突发事件应急响应系统数据库表结构

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名/学号/工号',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `role` VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT '角色：student-学生, teacher-教师, admin-管理员, commander-指挥员',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '邮箱',
    `department` VARCHAR(100) COMMENT '部门/院系',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 事件表
CREATE TABLE IF NOT EXISTS `event` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '事件ID',
    `title` VARCHAR(200) NOT NULL COMMENT '事件标题',
    `description` TEXT COMMENT '事件描述',
    `type` VARCHAR(50) NOT NULL COMMENT '事件类型：fire-火灾, earthquake-地震, medical-医疗, security-安全, other-其他',
    `level` VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '事件级别：low-低, normal-一般, high-高, urgent-紧急',
    `location` VARCHAR(200) COMMENT '事件地点',
    `reporter_id` VARCHAR(50) NOT NULL COMMENT '上报人ID',
    `reporter_name` VARCHAR(50) COMMENT '上报人姓名',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待处理, processing-处理中, resolved-已解决, closed-已关闭',
    `handler_id` VARCHAR(50) COMMENT '处理人ID',
    `handler_name` VARCHAR(50) COMMENT '处理人姓名',
    `report_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
    `handle_time` DATETIME COMMENT '处理时间',
    `resolve_time` DATETIME COMMENT '解决时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件表';

-- 资源表
CREATE TABLE IF NOT EXISTS `resource` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '资源ID',
    `name` VARCHAR(100) NOT NULL COMMENT '资源名称',
    `type` VARCHAR(50) NOT NULL COMMENT '资源类型：personnel-人员, equipment-设备, vehicle-车辆, material-物资, other-其他',
    `support_type` VARCHAR(50) COMMENT '支持类型：medical-医疗, security-安保, logistics-后勤, technical-技术, vehicle-车辆 等',
    `description` TEXT COMMENT '资源描述',
    `location` VARCHAR(200) COMMENT '资源位置',
    `quantity` INT DEFAULT 1 COMMENT '数量',
    `available_quantity` INT DEFAULT 1 COMMENT '可用数量',
    `status` VARCHAR(20) DEFAULT 'available' COMMENT '状态：available-可用, in_use-使用中, maintenance-维护中, unavailable-不可用',
    `contact_person` VARCHAR(50) COMMENT '联系人',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- 资源调度表
CREATE TABLE IF NOT EXISTS `resource_dispatch` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '调度ID',
    `event_id` VARCHAR(50) NOT NULL COMMENT '关联事件ID',
    `resource_id` VARCHAR(50) NOT NULL COMMENT '资源ID',
    `resource_name` VARCHAR(100) COMMENT '资源名称',
    `dispatch_quantity` INT NOT NULL COMMENT '调度数量',
    `dispatch_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '调度时间',
    `arrival_time` DATETIME COMMENT '到达时间',
    `status` VARCHAR(20) DEFAULT 'dispatched' COMMENT '状态：dispatched-已调度, in_transit-运输中, arrived-已到达, completed-已完成, cancelled-已取消',
    `dispatcher_id` VARCHAR(50) COMMENT '调度人ID',
    `dispatcher_name` VARCHAR(50) COMMENT '调度人姓名',
    `remark` TEXT COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源调度表';

-- 事件流程记录表
CREATE TABLE IF NOT EXISTS `event_process` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '流程记录ID',
    `event_id` VARCHAR(50) NOT NULL COMMENT '关联事件ID',
    `process_type` VARCHAR(50) NOT NULL COMMENT '流程类型：report/command/dispatch/handle/resolve/announce 等',
    `operator_id` VARCHAR(50) COMMENT '操作人ID',
    `operator_name` VARCHAR(50) COMMENT '操作人姓名',
    `process_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `process_note` TEXT COMMENT '流程说明/备注',
    INDEX idx_event_process_event_id (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件流程记录表';

-- 应急指挥表
CREATE TABLE IF NOT EXISTS `command` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '指挥ID',
    `event_id` VARCHAR(50) NOT NULL COMMENT '关联事件ID',
    `title` VARCHAR(200) NOT NULL COMMENT '指令标题',
    `content` TEXT NOT NULL COMMENT '指令内容',
    `commander_id` VARCHAR(50) NOT NULL COMMENT '指挥员ID',
    `commander_name` VARCHAR(50) COMMENT '指挥员姓名',
    `executor_id` VARCHAR(50) COMMENT '执行人ID',
    `executor_name` VARCHAR(50) COMMENT '执行人姓名',
    `priority` VARCHAR(20) DEFAULT 'normal' COMMENT '优先级：low-低, normal-一般, high-高, urgent-紧急',
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待执行, executing-执行中, completed-已完成, cancelled-已取消',
    `issue_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `execute_time` DATETIME COMMENT '执行时间',
    `complete_time` DATETIME COMMENT '完成时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应急指挥表';

-- 信息发布表
CREATE TABLE IF NOT EXISTS `announcement` (
    `id` VARCHAR(50) PRIMARY KEY COMMENT '发布ID',
    `event_id` VARCHAR(50) COMMENT '关联事件ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `type` VARCHAR(20) DEFAULT 'notice' COMMENT '类型：notice-通知, warning-预警, update-更新, other-其他',
    `target_audience` VARCHAR(50) DEFAULT 'all' COMMENT '目标受众：all-全部, student-学生, teacher-教师, staff-员工',
    `publisher_id` VARCHAR(50) NOT NULL COMMENT '发布人ID',
    `publisher_name` VARCHAR(50) COMMENT '发布人姓名',
    `status` VARCHAR(20) DEFAULT 'published' COMMENT '状态：draft-草稿, published-已发布, archived-已归档',
    `publish_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `view_count` INT DEFAULT 0 COMMENT '查看次数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='信息发布表';

-- 插入初始数据
INSERT INTO `user` (`id`, `username`, `password`, `name`, `role`, `phone`, `department`, `status`) VALUES
('user001', '2025001', '123456', '张三', 'student', '13800138001', '计算机学院', 1),
('user002', 'admin', 'admin123', '管理员', 'admin', '13800138002', '应急管理中心', 1),
('user003', 'commander001', '123456', '李指挥', 'commander', '13800138003', '应急指挥中心', 1),
('user004', 'teacher001', '123456', '王老师', 'teacher', '13800138004', '计算机学院', 1);
