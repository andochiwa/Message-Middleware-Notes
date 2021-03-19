# Index

1. [工作模式&核心组成](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/01_pattern)
2. [整合springboot](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/02_springboot)
3. [高级特性](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/03_features)
4. [分布式事务](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/04_distribution-transaction)



# 简介

RabbitMQ是一个开源的，基于Erlang语言编写，遵循AMQP协议实现的，支持多种客户端(语言)，用于在分布式系统中存储消息，转发消息，具有高可用，高扩展性，易用性等特征



# RabbitMQ角色分类

## 1.None

* 不能访问management plugin

## 2.Management: 查看相关节点信息

* 列出自己可以通过AMQP登入的虚拟机
* 查看自己的虚拟机节点virtual hosts的queues，exchanges和bindings信息
* 查看有关自己虚拟机节点virtual hosts的统计信息，包括其他用户在这个节点virtual hosts中的活动信息

## 3.Policymaker

* 包含management所有权限
* 查看，创建和删除自己的virtual hosts所属的policies和parameters信息

## 4.Monitoring

* 包含management所有权限
* 罗列出所有virtual hosts，包括不能登陆的
* 查看其他用户的connections和channels信息
* 查看节点级别的数据，如clustering和memory的使用情况
* 查看所有的virtual hosts的全局统计信息

## 5.Administrator

* 最高权限，什么都能做





# 一些面试题

> 可能存在没有交换机的队列吗？

不可能，即使没有指定交换机，也一定会存在一个默认的交换机



> RabbitMQ为什么使用信道，为什么不是TCP直接通信

1. TCP的创建和销毁开销大，需要进行三次握手和四次挥手
2. 如果不用信道，那应用程序就会TCP连接到Rabbit服务器，高峰时大量的连接会造成资源的浪费，而且低层OS每秒处理tcp连接数量也是有限制的，必然会造成性能瓶颈
3. 信道的原理是一条线程一条信道，多条线程多条信道同用一条TCP连接，一条TCP连接可以容纳无限的信道，即使每秒上万的请求也不会成为性能瓶颈



> queue在消费者创建还是生产者创建？

1. 一般建议在rabbitmq后台操作面板创建，是一种稳妥的做法
2. 一般来说在消费者这边创建比较好，消息的消费是在这边，这样可能生产在生产消息时丢失消息
3. 在生产者创建队列也可以，也是比较稳妥的方法，不会造成消息丢失
4. 也可以都两边创建，谁先启动谁先创建



> 在RabbitMQ的分布式事务中出现异常会发生什么事情？该如何解决？

分布式事务中出现异常时，会触发RabbitMQ的消息重试机制，造成死循环

解决消息重试的几种方案

1. 控制重发的次数，到达一定次数就移除，如果配置了死信队列则放进死信队列
2. try-catch + 手动ack
3. try-catch + 手动ack + 死信队列