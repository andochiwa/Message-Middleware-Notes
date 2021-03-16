# 目录

1. [队列&主题](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/01_helloworld)
2. [JMS](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/02_JMS)
3. [整合spring](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/03_spring)
4. [整合springboot](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/04_springboot)
5. [传输协议](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/05_protocol)
6. [持久化机制](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/06_persistence)
7. [可用性(集群配置)](https://github.com/andochiwa/Message-Middleware-Notes/tree/master/ActiveMQ/07_cluster)



# 异步投递，延迟投递，定时投递

## 1.异步投递

> 默认投递方式。
>
> 1. 可以最大化produce端的发送效率，通常在消息比较密集时使用
> 2. 需要较多的client断内存，也会导致broker端性能消耗增加
> 3. 不能确保消息发送成功，在useAsyncSend=true的情况下，客户端需要容忍消息丢失的可能性
> 4. 发送非事务的持久化消息时是同步的

丢失场景：

* 由于消息不阻塞，生产者会认为所有send的消息均被成功发送至MQ。**如果服务端突然宕机，此时生产者端内存中尚未被发送至MQ的消息都会丢失**

丢失消息时的解决方案

* 正常的异步发送方法都要接收回调
* produce.send方法中有个`AsyncCallback`接口，重写其`onSuccess`方法和`onException`方法可以获取到成功和失败的信息

## 2.延迟投递，定时投递

当我们不希望消息马上被broker投递出去，而是设置定时或者延时投递时，ActiveMQ提供了一种broker端消息定时的调度机制

首先需要在xml配置文件中设置`schedulerSupport="true"`

在消息类中有`setIntProperty`, `setLongProperty`等方法，可以设置`SheduledMessage`提供的静态属性，有四种属性

1. `AMQ_SCHEDULED_DELAY`(long) 延迟投递的时间
2. `AMQ_SCHEDULED_PERIOD`(long)  重复投递的时间间隔
3. `AMQ_SCHEDULED_REPEAT`(int) 重复投递次数
4. `AMQ_SCHEDULED_CRO`(String) Cron表达式



# 消费重试机制

> 某些情况下会引起消息重发，这时候需要设定重发的机制

## 1.引起重发的情况

1. Client端用了事务并且rollback()
2. Client端用了事务并且在调用commit()之前关闭或者没有commit
3. Client端在CLIENT_ACKNOWLEDGE传递模式下，调用了recover()

## 2.默认重发时间间隔和次数

* 间隔：1
* 次数：6

## 3.有毒消息

一个消息被重发超过最大重发次数时，消费端会给MQ发送一个`poison ack`表示消息有毒，告诉broker不要再发了，这时候broker会把这个消息放到DLQ(死信队列)

[具体配置官网](https://activemq.apache.org/redelivery-policy)

# 死信队列

> 一条消息被重发多次后(默认为6次)，将会被ActiveMQ移入死信队列，开发人员可以在这个Queue中查看出错的信息，进行人工干预

[具体配置官网](https://activemq.apache.org/message-redelivery-and-dlq-handling)