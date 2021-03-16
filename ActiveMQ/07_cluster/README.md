# 消息队列如何保证高可用性（集群）

>  Replicated LevelDB Store：基于zookeeper和LevelDB搭建ActiveMQ集群，集群仅提供主备方式的高可用集群功能，避免单点故障

# 三种集群方式

1. 基于sharedFileSystem共享文件系统
2. 基于JDBC
3. 基于可复制的LevelDB（Replicated LevelDB Store）

# Replicated LevelDB Store

从ActiveMQ5.9开始，集群实现方式取消了传统的Master-Slave方式，增加了Zookeeper+LevelDB的Master-Slave实现方式

## 配置步骤

1. 搭建zookeeper集群环境
2. 创建三个mq集群目录，代表为三个节点，并修改端口号（模拟集群环境）
3. activemq集群配置
   1. 三个节点不同的配置文件路径
   2. 三个节点BrokerName全部改为一致
   3. 三个节点的持久化配置(LevelDB)
4. 启动zk集群，启动mq集群