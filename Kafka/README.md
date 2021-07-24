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

# Kafka 架构深入

## 工程流程

![](img/5.png)

Kafka 中的消息是以 Topic 进行分类的，生产者生产消息，消费者消费消息，都是面向 Topic 的。

Topic 是逻辑上的概念，而 Partition 是物理上的概念，每个 Partition 对应一个 log 文件，该 log 文件中存储的是 producer 生产的数据。producer 生产的数据会被不断追加到该 log 文件的末端，且每条数据都有自己的 offset。消费者组中的每个消费者，都会实时记录自己消费到了哪个 offset，以便出错恢复时，从上次的位置继续消费。

## 文件存储机制

![](img/3.png)

<img src="img/4.png" style="zoom:100%;" />

由于生产者生产的消息会不断追加到 log 文件末端，为防止 log 文件过大导致数据定位效率低，kafka 采取了分片和索引机制，将每个 partition 分为多个 segment（逻辑上的概念，index + log 文件）

每个 partition（目录）相当于一个巨型文件被平均分配到多个大小相等的 segment（片段）数据文件中（每个 segment 文件中消息数量不一定相等），这种特性也方便 old segment 的删除，即方便已被消费的消息的清理，提高磁盘的利用率。每个 partition 只需要支持顺序读写就行，segment 的文件生命周期由服务端配置参数（log.segment.bytes，log.roll.{ms,hours}等若干参数）决定

每个 segment 对应两个文件 ----“.index” 和 “.log” 文件。分别表示为 segment 索引文件和数据文件（引入索引文件的目的就是便于利用二分查找快速定位 message 位置）。这两个文件的命名规则为：

partition全局的第一个 segment 从 0 开始，后续每个 segment 文件名以当前 segment 的第一条消息的 offset 命名，数值大小为 64 位，20 位数字字符长度，没有数字用 0 填充。

这些文件位于一个文件夹下（partition目录），改文件夹的命名规则：topic 名+分区序号。例如，first 这个 topic 有三个分区，则其对应的文件夹为first-0，first-1，first-2

<img src="img/6.png" style="zoom:150%;" />

og 文件和 index 文件都是以当前文件中的最小偏移量的值命名。index 文件存储大量的索引信息，log 文件存储大量的数据，**索引文件中的元数据对应数据文件中消息的物理偏移地址**。查找消息的时候，会根据 offset 值以二分查找的方式查找对应的索引文件，找到消息在 log 文件中的偏移量，最终找到消息。这也就能够保证，消费者挂掉重启的时候，可以根据 offset 值快速找到上次消费的断点位置。

## 生产者分区策略

### 分区的原因

1. **方便在集群中扩展**，每个 Partition 可以通过调整以适应它所在的机器，而一个 Topic 又可以由多个 Partition 组成，因此整个集群可以适应任意大小的数据
2. **提高并发**，因为可以以 Partition 为单位读写数据

### 分区的原则

我们需要将 Producer 发送的数据封装成一个`ProducerRecord`对象

1. 指明 Partition 的情况下，直接将指明的值作为 Partition 的值
2. 没有指明 Partition 值但有 Key 的情况下，将 Key 的 hash 值与 Topic 的 Partition 数进行取余得到 Partition 值
3. 既没有 Partition 的值又没有 Key 值的情况下，第一次调用时随机生成一个整数，将这个值与 Topic 可用的 Partition 总数取余得到 Partition，也就是 round-robin 算法

