# IdleGoodsInfo 二手物品信息平台

## 一、系统结构设计
- **MVC 分层**：
  
  - Model：`idlegoodsinfo.entity` 目录下的 `User` 与 `Listing` Pojo 关系映射字段。
  - DAO：`idlegoodsinfo.dao` 包含操作用户和物品的接口，具体实现由 `impl` 包下的 `JdbcUserDao` 与 `JdbcListingDao` 负责与 MySQL 交互。
  - Services：`UserService` 和 `ListingService` 在业务层封装验证、密码哈希（`PasswordHasher`）及事务控制，供 Servlet 调用。
  - Controllers：`
    - `AuthServlet`（登录/注册）与 `ListingServlet`（发布/编辑/删除/查询）分别负责处理请求、调用 Service 并转发 JSP 视图。
    - `StartupListener` 初始化 HikariCP 数据源，确保连接池可用。
  
- **前端页面**：JSP + JSTL 组合，【没有】嵌入 Java 代码。

  ​		静态资源（CSS等）统一放在 `webapp/static`，通过相对路径如 +`static/css/styles.css` 引用。

- **Session 管理**：登录后通过 `HttpSession` 跟踪当前用户，重复登录自动退出 并 提供手动退出功能。

## 二、数据库结构说明
数据库名：`idlegoodsinfo`，字符集 `utf8mb4` 以支持多语言。注意：COLLATE 项使用ci参数实现不区分大小写

常用字段如下：

### users 表
| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | BIGINT AUTO_INCREMENT | 主键 |
| `username` | VARCHAR(50) | 唯一用户名，排序规则建议使用 `utf8mb4_unicode_ci` 以支持模糊查询同时不区分大小写 |
| `password_hash` | VARCHAR(100) | BCrypt 哈希后的密码 |
| `created_at` | TIMESTAMP | 自动记录注册时间 |

排序语句示例：
```sql
ALTER TABLE users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### listings 表
| 字段 | 类型 | 说明 |
|---|---|---|
| `id` | BIGINT AUTO_INCREMENT | 主键 |
| `user_id` | BIGINT | 外键，关联 `users(id)` |
| `title` | VARCHAR(100) | 物品标题，兼容 `utf8mb4_unicode_ci` 以实现不区分大小写的模糊查询 |
| `description` | TEXT | 物品描述 |
| `quantity` | INT | 库存数量 |
| `created_at` / `updated_at` | TIMESTAMP | 自动填充创建/更新时间 |

## 三、部署方法
1. **准备环境**
   -  JDK 21、Maven 3.8+；
   - MySQL 5.7，创建数据库 `idlegoodsinfo`；
   - 数据库中使用 `schema.sql` 执行建表语句。
2. **配置数据库连接**
   - `StartupListener` 中的 `jdbcUrl`、用户名、密码要改为目标服务器实际值。
3. **构建项目**
   - 执行 `./mvnw.cmd clean package`生成 `target/IdleGoodsInfo.war`。
4. **部署与访问**
   - 将生成的 WAR 文件部署到 Tomcat（注意部署名即为上下文路径）。
   - 访问 `http://<host>:8080/<context>/login.jsp` 登录或注册，首页可发布、编辑、删除自己的物品；支持关键字模糊查询且自动展现编辑/删除按钮。

## 四、系统使用方法

​	1.用户注册/登录账户

​	2.进入主界面，可以直接看到、模糊查询别人发布的内容

​	3.可以发布自己的新帖子、编辑/删除自己拥有的帖子内容

## 五、测试用登录账户

1. 测试用账户：

   user ：	`1`

   pwd :	   `1`
