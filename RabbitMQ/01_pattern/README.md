# rabbitMQ的核心组成

* `Server` 又称`Broker`，接受客户端的连接，实现AMQP实体服务，安装rabbitmq-server
* `Connection` 连接：应用程序与Broker的网络连接，即TCP/IP的三次握手和四次挥手
* `Channel` 网络信道：几乎所有的操作都在Channel中进行，Channel是进行消息读写的通道，客户端可以建立多个Channel，每个Channel代表一个会话任务
* `Message` 消息：服务与应用程序之间传送的数据，由`Properties`和`Body`组成，`Properties`可以对消息进行修饰，比如优先级，延迟等高级特性，`Body`是消息体的内容
* `Virtual Host` 虚拟地址：用于进行逻辑隔离，最上层的消息路由，一个虚拟主机理由可以有若干个Exchange和Queue，同一个虚拟主机里不能有相同名字的Exchange
* `Exchange` 交换机：接收消息，根据路由key发送消息到绑定的队列，不具备消息存储的能力
* `Bindings` Exchange和Queue之间的虚拟连接，Binding中可以保护多个routing key
* `Routing Key` 路由规则，虚拟机可以用它来确定路由的一个特定消息
* `Queue` 消息队列：保存消息转发给消费者

# rabbitMQ的工作模式

## Simple模式

一个生产者，一个消费者

## work模式

一个生产者，多个消费者，每个消费者获取到的消息唯一

## Publish/Subscribe模式

一个生产者发送的消息会被多个消费者获取，是一种广播机制，没有路由key的模式，如果消息发送到没有队列绑定的交换机上，那么消息将丢失

## Routing模式

有路由key的匹配模式

1. 发送消息到交换机并且要指定路由key
2. 消费者将队列绑定到交换机时需要指定路由key

## Topics模式

模糊的路由key匹配模式，将路由键和某模式进行匹配，此时队列需要绑定在一个模式上，“#”匹配一个词或多个词，“*”只匹配一个词

## RPC模式

RPC是一种远程过程调用协议(Remote Procedure Call Protocol)，部署在不同的机器上的服务需要网络通信时，RPC协议定义了规划，可以高效的实现。使用MQ可以实现RPC的异步调用，基于Direct交换机实现

## Publisher Confirms模式

发布确认模式，与发布者进行可靠的发布确认，和事务类似