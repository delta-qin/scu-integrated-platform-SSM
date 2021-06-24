#  校园综合平台

## 必要性
- 现有平台难找不纯粹，安全机制不完善
- 基本没人维护

## 系统分层架构

## 项目技术
kafka、ES、MySQL、Redis、Caffeine、Spring Security、Quartz、Spring Mail、Mybatis、SpringBoot、MongoDB、Netty、webSocket

## 项目进度

### v1.0 基于牛客高级项目的基本实现 (已完成)
- 游客
    - 浏览帖子
- 用户
    - 发布帖子。评论。回复。私信
    - 点赞。关注用户
- 版主
    - 加精。置顶帖子
- 管理员
    - 删除帖子
    
使用的技术：kafka、ES、MySQL、Redis、Caffeine、Spring Security、Quartz、Spring Mail、Mybatis、SpringBoot
    
### v2.0  MongoDB 替换 MySQL 存储评论 (已完成)

使用的技术：MongoDB

#### 为什么
文章评论两项功能存在以下特点：数据量大，写入操作频繁，价值较低。对于这样的数据，我们更适合使用MongoDB来实现数据的存储

MongoDB是一个基于分布式文件存储的数据库。由C++语言编写。旨在为WEB应用提供可扩展的高性能数据存储解决方案。 MongoDB是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。它支持的数据结构非常松散，是类似json的bson格式，因此可以存储比较复杂的数据类型。

[](note/v2.0使用mongoDB改进.md)

### v3.0 ， 使用websocket + netty 替换项目的私信和系统通知功能

#### 为什么

- 一开始私信和系统通知是使用消息队列异步消费到数据库或者Redis里面，用户自己手动刷新才可以看到新消息
- 使用websocket + netty的方式只要用户在线（websocket连接建立），即使用户不刷新，也可以得到新消息的提示

#### 一开始的消息异步消费到数据库用户刷新适合不紧急 

- 比如说用户刷新页面才去看看有没有什么自己关注的新的消息

### v4.0 ， 使用DDD领域模型（充血）对项目重构

## 项目结构
```
.
├── README.md
├── Solution.md
├── scu-application 应用层
├── scu-common 公共
├── scu-domain 领域层
├── scu-infrastructure 基础设施层
├── scu-interfaces rpc接口层 + 参数校验（设计在这里rest也可以使用）
├── scu-protal rest接口层
└── scu-starter 启动器

```

## 项目运行

只需要启动starter里面的项目即可

## 项目部署

[部署文档](deploy.md) 

## 压力测试

[压测项目](jmeter) 

## 常见问题


## 设计
- 点赞和评论都有可能针对文章和评论，为了区分二者又为了少写代码，直接抽象为实体，1表示文章，2表示评论，ID就是对应的ID直接传递即可，这样虽然多了一个字段，但是只需要一份代码
- 拦截器填充ThreadLocal 实现用户信息的使用
- 校验信息，生成登录凭证到Redis，后面拦截器会使用凭证验证用户是否登录，如果登录了还会将用户的信息设置到security，方便授权。所以登录是使用自己的，授权使用的是security
- 消息 + kafka 实现ES的数据同步
- 图片云服务，生成分享是服务器上传，上传头像是客户端上传

## 参考
- forum-java：项目结构设计
- 牛客网高级项目：前端页面，后端部分逻辑
- 十次方论坛项目：MongoDB 以及 websocket+netty
