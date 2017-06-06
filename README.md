# qiaoyy-service


## 6-6

### 石头剪刀布进入测试修改bug阶段
服务器更新当前最新服务


## 6-5

### 添加Globals全局服务管理
全局服务交给Globals管理，所有全局的服务可以添加到这，通过Globals方便获取，参考TimeService和ScheduleService

### 改进Channel管理
Channel添加心跳管理，默认时间内没有响应则踢下线，防tcp半连接资源占用
ChannelUser对象的userid改为Player对象
Player为新增的玩家内存缓存（目前没有需要缓存的数据，如果需要缓存，往这里加），Player生命周期与Channel一致
ChannelUser的注册改为连接时注册，在ws连接时需传递参数userid来注册ChannelUser，否则不能正确建立连接
ws的handler处理逻辑时应按照指定各位传递json字符串，否则不能正确解析

### 添加定时任务服务
添加全局心跳，服务器指定时间执行一次tick，驱动所有的定时服务
目前只驱动channel心跳和更新时间缓存

### 添加时间服务
TimeService为时间服务，对事件缓存，解决系统时间获取的效率问题
例如获取当前时间：Globals.getTimeService().now();

### 修改WS逻辑接入方式
添加Api枚举类，所有接口需要在枚举类中定义协议号和该接口使用线程
添加GameDispatcher，所有逻辑分发在GameDispatcher的dispatch方法中分发，统一逻辑分发入口，具体方式参考TEST协议和STONE协议

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

<s>数据库选择mysql 连接 mybatis</s>选用jpa

