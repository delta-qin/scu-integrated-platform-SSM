#  校园综合平台

## 必要性
- 现有平台难找不纯粹，安全机制不完善
- 基本没人维护

## 系统分层架构

## 项目技术

## 实现思路

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
