# 校园突发事件应急响应系统

## 项目简介

这是一个基于 SpringBoot + MySQL + Vue3 开发的校园突发事件应急响应系统，主要功能包括：

- **事件上报**：学生、教师可以上报校园突发事件
- **资源调度**：管理员可以调度应急资源（人员、设备、车辆、物资等）
- **应急指挥**：指挥员可以发布应急指令，相关人员执行指令
- **信息发布**：发布应急通知、预警信息等

## 技术栈

- **后端**：Spring Boot 4.0.1 + MyBatis + MySQL
- **前端**：Vue 3 (CDN方式) + HTML + CSS
- **数据库**：MySQL 8.0+

## 项目结构

```
emergence-event/
├── src/
│   ├── main/
│   │   ├── java/com/lynn/emergence/
│   │   │   ├── controller/     # 控制器层
│   │   │   ├── service/        # 服务层
│   │   │   ├── mapper/         # MyBatis Mapper接口
│   │   │   └── entity/          # 实体类
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML映射文件
│   │       └── templates/      # 前端HTML页面
│   └── test/
├── sqls/
│   └── ddl/
│       └── emergence_event.sql # 数据库表结构
└── pom.xml
```

## 快速开始

### 1. 环境要求

- JDK 21+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE emergence_event CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行SQL脚本：
```bash
mysql -u root -p emergence_event < sqls/ddl/emergence_event.sql
```

3. 修改数据库配置（`src/main/resources/application.yml`）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/emergence_event?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的数据库密码
```

### 3. 运行项目

```bash
# 使用Maven运行
mvn spring-boot:run

# 或者使用Maven Wrapper
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

### 4. 访问系统

- 登录页面：http://localhost:8080/
- 主页面：http://localhost:8080/main.html

## 默认账号

系统已预置以下测试账号：

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| 2025001 | 123456 | student | 学生 |
| admin | admin123 | admin | 管理员 |
| commander001 | 123456 | commander | 指挥员 |
| teacher001 | 123456 | teacher | 教师 |

## 功能说明

### 1. 事件上报

- 学生、教师可以上报校园突发事件
- 支持选择事件类型（火灾、地震、医疗、安全、其他）
- 支持设置事件级别（低、一般、高、紧急）
- 可以查看自己的上报记录

### 2. 资源调度

- 查看可用资源列表
- 为事件调度资源（人员、设备、车辆、物资等）
- 跟踪资源调度状态（已调度、运输中、已到达、已完成）
- 自动更新资源可用数量

### 3. 应急指挥

- 指挥员可以发布应急指令
- 指令可以关联到具体事件
- 支持设置优先级（低、一般、高、紧急）
- 相关人员可以执行指令并标记完成

### 4. 信息发布

- 发布应急通知、预警信息等
- 支持选择信息类型和目标受众
- 记录查看次数
- 可以查看已发布的信息列表

## API接口

### 用户相关
- `POST /api/login` - 用户登录

### 事件相关
- `GET /api/event/list` - 获取事件列表
- `GET /api/event/{id}` - 获取事件详情
- `POST /api/event/report` - 上报事件
- `PUT /api/event/handle/{id}` - 处理事件
- `PUT /api/event/resolve/{id}` - 解决事件

### 资源相关
- `GET /api/resource/list` - 获取资源列表
- `GET /api/resource/{id}` - 获取资源详情
- `POST /api/resource/save` - 保存资源
- `DELETE /api/resource/{id}` - 删除资源

### 资源调度相关
- `GET /api/dispatch/list` - 获取调度列表
- `POST /api/dispatch/dispatch` - 调度资源
- `PUT /api/dispatch/arrive/{id}` - 标记资源到达
- `PUT /api/dispatch/complete/{id}` - 完成调度

### 应急指挥相关
- `GET /api/command/list` - 获取指令列表
- `POST /api/command/issue` - 发布指令
- `PUT /api/command/execute/{id}` - 执行指令
- `PUT /api/command/complete/{id}` - 完成指令

### 信息发布相关
- `GET /api/announcement/list` - 获取信息列表
- `GET /api/announcement/{id}` - 获取信息详情
- `POST /api/announcement/publish` - 发布信息
- `DELETE /api/announcement/{id}` - 删除信息

## 数据库表结构

- `user` - 用户表
- `event` - 事件表
- `resource` - 资源表
- `resource_dispatch` - 资源调度表
- `command` - 应急指挥表
- `announcement` - 信息发布表

详细表结构请查看 `sqls/ddl/emergence_event.sql`

## 开发说明

### 添加新功能

1. 在 `entity` 包下创建实体类
2. 在 `mapper` 包下创建 Mapper 接口和 XML 映射文件
3. 在 `service` 包下创建 Service 类
4. 在 `controller` 包下创建 Controller 类
5. 在前端页面添加相应的功能模块

### 代码规范

- 使用驼峰命名法
- Controller 层负责接收请求和返回响应
- Service 层负责业务逻辑处理
- Mapper 层负责数据库操作

## 注意事项

1. 确保 MySQL 服务已启动
2. 确保数据库连接配置正确
3. 首次运行前需要执行 SQL 脚本创建表结构
4. 前端使用 Vue3 CDN 方式，无需额外构建步骤

## 许可证

MIT License

