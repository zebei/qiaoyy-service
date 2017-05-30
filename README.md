# qiaoyy-service

## 5-30

### 后台框架改为Springboot管理，使用内置tomcat
配置文件说明：
|--config
|-----application-dev.properties 开发环境配置文件
|-----application-prod.properties 生产环境配置文件
|--application.properties 指定本地开发使用的默认环境
|--log4j2-dev.xml 开发环境log4j2配置文件（不会生成log文件，只在控制台输出）
|--log4j2-prod.xml 开发环境log4j2配置文件（只输出到log文件）
|--sdk.config SDK配置文件，首次部署环境需要放到spring配置文件中对应的地方

### 添加线程池管理
核心类
ThreadPool 线程定义和分发，根据不同业务使用不同类型线程池
ThreadType 线程类型，根据线程类型分发
ThreadPoolMonitor 线程监控，用于打印线程信息
ThreadPoolExceptionHandler 线程异常

### 添加Netty实现的WebSocket
使用Netty实现WS服务，端口可在配置文件配置
核心类
WebSocketServer WS服务启动和关闭
WebSocketServerHandler WS服务处理入口，逻辑入口在channelRead0方法

### 数据存储改为Spring jpa方式
spring-data-jpa的方式让数据访问更简单，使用方式可参照User的使用

## 5-27

配置文件 Linux路径"/etc/qcloud/sdk.config";

重启服务器tomcat      systemctl restart tomcat

<Context path="" docBase="com.qcloud.weapp.demo">

                <WatchedResource>WEB-INF/web.xml</WatchedResource>

</Context>



## 5-26

初始项目构建完成
登陆用户入库

将sdk 融入到项目中 不在引入jar包

log 选用log4j  

数据库选择mysql 连接 mybatis

