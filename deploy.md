## mac dev

### canal（暂时不用）

~~开启数据库的binlog~~

~~新建canal用户，设置权限~~

~~在canal中设置server信息以及消费的方式是ES~~

### kafka

http://kafka.apache.org/downloads 最新版

https://www.apache.org/dyn/closer.cgi?path=/kafka/2.8.0/kafka_2.12-2.8.0.tgz

```
# 配置zk
vi config/zookeeper.properties

dataDir=/Users/deltaqin/Desktop/kafka_2.12-2.8.0/data/zk

# 配置kafka
vi config/server.properties

log.dirs=/Users/deltaqin/Desktop/kafka_2.12-2.8.0/data/kafka-logs

# 启动zk
sh zookeeper-server-start.sh ../config/zookeeper.properties

# 启动kafka
sh kafka-server-start.sh ../config/server.properties

# 关闭
bin/zookeeper-server-stop.sh
bin/kafka-server-stop.sh
```

测试

```
# 创建主题
sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test

# 当前有的主题
sh kafka-topics.sh --list --bootstrap-server localhost:9092

# 指定broker位置之后，加入命令行，开始发消息
sh kafka-console-producer.sh --broker-list localhost:9092 --topic test

# 启动消费者
sh kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
```

需要创建程序对应的主题

```
sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic  like

sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic follow

sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic comment

sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic share

sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic delete

sh kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic publish
```

配置文件修改application-develop.properties

```
# KafkaProperties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=community-consumer-group
spring.kafka.consumer.enable-auto-commit=true
spring.kafka.consumer.auto-commit-interval=3000
```



### mysql

直接使用Mac安装的MySQL8即可

https://juejin.cn/post/6844903870053761037#heading-1

导入数据库，在sql文件夹

配置文件修改application-develop.properties

```
# DataSourceProperties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/community?characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=000000
spring.datasource.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.hikari.maximum-pool-size=15
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=30000
```



### redis

用的虚拟机的Redis，Mac自行安装

```
wget    http://download.redis.io/releases/redis-5.0.5.tar.gz
# 解压进入目录
yum install gcc
make
mkdir -p /opt/deltaqin/redis5
make install PREFIX=/opt/deltaqin/redis5
vi /etc/profile

export  REDIS_HOME=/opt/deltaqin/redis5
export PATH=$PATH:$REDIS_HOME/bin

source /etc/profile
cd utils
./install_server.sh

service   redis_6379  start/stop/stauts     

# 默认下
# 配置文件/etc/redis/6379.conf
# 日志文件 /var/log/redis_6379.log
# 可执行文件 /opt/deltaqin/redis5/bin/redis-server
```

### ES

在Linux虚拟机上安装的

https://www.elastic.co/downloads/past-releases#elasticsearch

```
tar -zxvf elasticsearch-6.4.3.tar.gz

useradd deltaqin
passwd deltaqin
cp -r elasticsearch /home/deltaqin/
cd /home/deltaqin/
chown deltaqin:deltaqin elasticsearch -R
```

```
vim jvm.options

-Xms512m
-Xmx512m
```

```
vim elasticsearch.yml
cluster.name:nowcoder # 集群的名字

path.data: /home/deltaqin/elasticsearch/data # 数据目录位置
path.logs: /home/deltaqin/elasticsearch/logs # 日志目录位置

network.host: 0.0.0.0 # 绑定到0.0.0.0，允许任何ip来访问
```

```
mkdir data
```

```
# 修改文件描述符限制
vim /etc/security/limits.conf

* soft nofile 65536

* hard nofile 131072

* soft nproc 4096

* hard nproc 4096
```

```
# 修改虚拟内存限制
vim /etc/sysctl.conf
vm.max_map_count=655360

# 执行
sysctl -p
```

```
# 中文分词
# 下载的时候要下载有jar的，不要下载源码，下载对应版本
https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v6.4.3

解压到plugins 下的 ik文件夹
```

```
后台启动
非root的家目录里面一般是
bin/elasticsearch -d

关闭
使用REST APIcurl -XPOST 'http://localhost:9200/_shutdown'.
或者直接kill

```
配置文件修改

```
# ElasticsearchProperties
spring.data.elasticsearch.cluster-name=nowcoder
# 9200 是 http访问的端口
spring.data.elasticsearch.cluster-nodes=10.211.55.6:9300
```



更详细参考：

https://www.yuque.com/docs/share/3af37806-2a9b-436a-a71b-2f2292fd2833?# 《项目练习（十一）Elastic索引数据库》

### wkhtmltopdf

https://wkhtmltopdf.org/downloads.html

下载安装即可

```
wkhtmltopdf https://www.nowcoder.com ~/Desktop/1.pdf
wkhtmltoimage https://www.nowcoder.com ~/Desktop/1.png
wkhtmltoimage --quality 75 https://www.nowcoder.com ~/Desktop/2.png
```

配置文件修改

```
wk.image.command=/usr/local/bin/wkhtmltoimage
wk.image.storage=/Users/deltaqin/workspace/myworkspace2021/code2021/project/nowcoder-project-master/community/wk-images
```



## win dev

参考Mac，网上找对应的win安装

## linux deploy 

### kafka

设置zookeeper不用改

设置server，不用改

```
# 启动zk
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

# 启动kafka
nohup bin/kafka-server-start.sh config/server.properties 1>/dev/null 2>&1 &
# 测试
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```



### mysql

https://juejin.cn/post/6844903870053761037#heading-1

第一种yum即可

### redis

上面有

### ES

上面有

### wkhtmltopdf

```
yum list wkhtmltopdf*
yum install ...
yum list *xvfb*  服务器没有 GUI程序，选择xorg-xll-server-xvfb....
xvfb-run --server-args="-screen 0, 1024x768x24" wkhtmltoimage https://www.baidu.com 1.png

vi run.sh
xvfb-run --server-args="-screen 0, 1024x768x24" wkhtmltoimage "$@"

chmod +x run.sh
run.sh   https://www.baidu.com 2.png
```

### tomcat

下载解压

```
vi /etc/profile
export PATH=$PATH:/opt/tomcat/bin

source /etc/profile
```

```
startup.sh
```

```
cd webapps 
```

默认是8080

### nginx

```
yum list nginx*
```

```
vi /etc/nginx/nginx.conf

upstream myserver{  
  server ip:port  max_fails=3 fail_timeout=30s;
}

server {   
  listen 80;    
  server name ...;    
  location / {       
    proxy_pass http://myserver;    
  }
}
```

```
nginx -s reload
```



### 项目修改

pom.xml

```
	<packaging>war</packaging>


	<build>
<!--		 打包的名字！！！！！！！！！！-->
		<finalName>ROOT</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

配置文件

```

# profile
spring.profiles.active=develop

# logback
logging.config=classpath:logback-spring-${spring.profiles.active}.xml

```



```
# 设置servlet上下文的路径为空，配置文件以及前端的global.js
# tymeleaf缓存要开启
# path.domain要修改
# wk的路径
```



Tomcat启动配置(已经设置)

打包

```
mvn clean package -Dmaven.test.skip=true
# 移动target下面的war到/tomcat的webapps下面
```

## 邮箱
### 163
spring.mail.port不指定；spring.mail.password不是邮箱密码，需要登录mail.163.com，前往设置 客户端授权密码中获取的一个16个字符的密码，同时允许POP3/SMTP服务。
```
# MailProperties
spring.mail.host=smtp.163.com
spring.mail.port= 
spring.mail.username=delta_qin@163.com
spring.mail.password=USGXDANNZYCLLUHU

# community
# 域名用于验证邮件变为可配置的
community.path.domain=http://localhost:8080 # 激活邮件会使用，修改为服务器地址
```

### qq

spring.mail.password不是QQ密码，登录mail.qq.com，前往设置 账户 POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务开启POP3/SMTP服务获取一个16个字符的密码

```
# MailProperties
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=736514834@qq.com
spring.mail.password=elrcwamfutgubfdh

```

参考：https://blog.csdn.net/weixin_43272781/article/details/104340720



## 七牛云

- 注册
- 设置公钥
- 创建空间
- 配置空间名字以及对应的域名

```
# qiniu
# 用户中心查看
qiniu.key.access=
qiniu.key.secret=秘钥
# 对应的空间查看
qiniu.bucket.header.name=deltaqin-community-header
quniu.bucket.header.url=http://qvt.hn-bkt.clouddn.com
qiniu.bucket.share.name=deltaqin-community-share
qiniu.bucket.share.url=http://qkts.hn-bkt.clouddn.com

```


## jmeter压测

使用jmeter打开项目下面的jmeter项目

即可测试

测试主要是针对热门帖子的三级缓存
