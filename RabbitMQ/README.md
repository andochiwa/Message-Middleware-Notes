# Index

1. [工作模式&核心组成](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/01_pattern)
2. [整合springboot](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/RabbitMQ/02_springboot)



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