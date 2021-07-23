# Kafka 概述

## 定义

Kafka 是一个**分布式**的基于**发布/订阅模式**的消息队列，主要用于大数据实时处理领域

## 消息队列两种模式

1. 点对点模式

生产者生产消息发送到 Queue 中，然后消息消费者从 Queue中取出并消费。消息被消费后，Queue 中不再有存储，所以消息消费者不可能消费到已经被消费的消息。 Queue 支持存在多个消费者，但是对一个消息而言，只有一个消费者可以消费。

2. 发布/订阅模式

发布者将消息发布到 Topic 中，同时有多个消息订阅者消费该消息。和点对点模式不同，发布到 Topic 的消息会被所有订阅者消费。

## Kafka 基础架构

![Disaster Recovery for Multi-Region Kafka at Uber | Uber Engineering Blog](img/1.png)

<img src="img/2.png" style="zoom:125%;" />

### 1. Producer

消息生产者，将消息 push 到 Kafka 集群中的 Broker。

### 2. Consumer

消息消费者，从 Kafka 集群中 pull 消息，消费消息。

### 3. Consumer Group

消费者组，由一到多个 Consumer 组成，每个 Consumer 都属于一个 Consumer Group。消费者组在逻辑上是一个订阅者。
消费者组内每个消费者负责消费不同分区的数据，一个分区只能由一个组内消费者消费；消费者组之间互不影响。
即每条消息只能被 Consumer Group 中的一个 Consumer 消费；但是可以被多个 Consumer Group 组消费。这样就实现了单播和多播。

### 4. Broker

一台 Kafka 服务器就是一个 Broker,一个集群由多个 Broker 组成，每个 Broker 可以容纳多个 Topic.

### 5. Topic

消息的类别或者主题，逻辑上可以理解为队列。Producer 只关注 push 消息到哪个 Topic, Consumer 只关注订阅了哪个 Topic。

### 6. Partition

负载均衡与扩展性考虑，一个 Topic 可以分为多个 Partition,物理存储在 Kafka 集群中的多个 Broker 上。可靠性上考虑，每个 Partition 都会有备份 Replica。

### 7. Replica

Partition 的副本，为了保证集群中的某个节点发生故障时，该节点上的 Partition 数据不会丢失，且 Kafka 仍能继续工作，所以 Kafka 提供了副本机制，一个 Topic 的每个 Partition 都有若干个副本，一个 Leader 和若干个 Follower。

### 8. Leader

Replica 的主角色，Producer 与 Consumer 只跟 Leader 交互。

### 9. Follower

Replica 的从角色，实时从 Leader 中同步数据，保持和 Leader 数据的同步。Leader 发生故障时，某个 Follower 会变成新的 Leader。

### 10. Controller

Kafka 集群中的其中一台服务器，用来进行 Leader election 以及各种 Failover（故障转移）。

### 11. ZooKeeper

Kafka通过Zookeeper存储集群的meta等信息。

# Kafka 快速入门

## 安装

必须先启动zookeeper，有点儿麻烦，所以我选择

**Docker真香！**

使用主目录文件`docker-compose.yml`然后 docker compose 直接安装即可！

## 命令行操作

```bash
# 在zookeeper创建一个分区为2，集群复制为1的test topic
kafka-topics.sh --create --zookeeper zookeeper:2181 --topic test --partitions 2 --replication-factor 1 
# 删除test topic
kafka-topics.sh --delete --zookeeper zookeeper:2181 --topic test 
# topic list
kafka-topics.sh --list --zookeeper zookeeper:2181
# 在topic上生产
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test
# 在topic上消费
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test
```

