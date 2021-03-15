# JMS

> JMS，全称为Java Message Service，即Java消息服务，是JavaEE中13个核心工业规范标准之一

|       1        |      2       |
| :------------: | :----------: |
| [组成结构](#1) | [可靠性](#2) |



# <span id="1">JMS的组成结构</span>

## 1.JMSMessage

### 消息头

* `JMSDestination` 消息发送的目的地，主要指Queue和Topic
* `JMSDeliveryMode` 持久模式和非持久模式
  * 持久性的信息：应该被传送**一次且仅一次**，意味着如果JMS的提供者出现故障，该消息不会丢失，它会在服务器恢复之后再次传递
  * 非持久性的消息：最多会传送一次，意味着如果服务器出现故障，该消息会**永久丢失**
* `JMSExpiration` 
  * 可以设置消息在一定时间后过期，默认永不过期
  * 等同于Destination方法中的timeToLive值加上发送时刻的GMT时间值。
  * 如果为0，则表示消息永不过期，如果超时，则消息被清除
* `JMSPriority` 消息优先级
  * 0-9十个级别，0-4为普通消息，5-9为加急消息，默认为4级
  * JMS不要求MQ严格按照优先级发送消息，但必须保证加急消息优先于普通消息
* `JMSMessageID`**重要** 唯一识别每个消息的标示由MQ产生

### 消息体

> 消息体是封装具体的消息数据，发送和接受的消息体类型必须一致对应

有五种消息数据格式

* `TextMessage` 普通字符串信息，包含一个String
* `MapMessage` Map类型信息，key为String，值为java的基本类型
* `BytesMessage` 二进制数组信息，包含一个byte[]
* `StreamMessage` java数据流信息，用标准流操作来顺序的填充和读取
* `ObjectMessage` 对象信息，包含一个可序列化的java对象

### 消息属性

如果需要除了消息头字段以外的值，那么可以使用消息属性

对于识别、去重、重点标注等很有效

setxxxProperty

## 2.JMSProvider

实现JMS接口和规范的消息中间件，也就是MQ服务器

## 3.JMSProduce

消息生产者，创建和发送JMS消息的客户端应用

## 4.JMSConsumer

消息消费者，接受和处理JMS消息的客户端应用

# <span id="2">JMS的可靠性</span>

* 持久性(Persistent)

  > `producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT)` 非持久化，当服务器宕机，则消息不存在
  >
  > `producer.setDeliveryMode(DeliveryMode.PERSISTENT)` 持久化，服务器宕机时，消息依然存在

  * `持久化Queue` 保证消息只被传送一次和成功一次，对于这些消息，**可靠性**是优先考虑因素，可靠性的另一个重要方面是确保持久化消息传送至目标后，消息服务在向消费者传送它们之前不会丢失这些消息，**默认为持久化**
  * `持久化Topic` 先运行一次订阅者，等于向MQ注册，再运行生产者发送消息，此时，无论订阅者是否在线都会接收到，不在线的话，下次连接会把所有消息都接受下来

  > `connection.createSession(boolean transacted, int acknowledgeMode)` 第一个参数是事务，第二个参数是签收
  >
  > 事务偏生产者，签收偏消费者
  >
  > 事务＞签收

* 事务(Transaction) producer提交时的事务

  * `false` 只要执行send，就进入到队列中，关闭事务需要第二个签收参数的设置有效
  * `true` 先执行send再执行commit，消息才被真正的提交到队列中。消息需要批量发送，所以要缓冲区处理
  * 注意！如果消费者开启了事务，却没有提交，那么消费者可以消费，但消息不会出队列，而消费者可以多次消费。

* 签收(Acknowledge)

  * 非事务
    * `AUTO	_ACKNOWLEDGE` 自动签收
    * `CLIENT_ACKNOWLEDGE` 手动签收 客户端调用acknowledge方法手动签收
    * `DUPS_OK_ACKNOWLEDGE` 允许重复消息
  * 事务
    * 事务开启后，只有commit后才能讲全部消息变为消费
    * 当一个事务被成功提交则消息会自动签收。如果事务回滚，则消息会被再次发送