# 传输协议

> ActiveMQ支持的传输协议有：`TCP`、`NIO`、`UDP`、`SSL`、`Http(s)`、`VM`
>
> 默认支持的消息协议有：`tcp`、`amqp`、`stomp`、`mqtt`、`ws`

在activeMQ安装目录中的conf/activemq.xml中的&lt;transportConnectors&gt;标签内配置

默认消息协议是`openwire`，所以tcp的URI头不采用tcp而是openwire

另外，如果不是特别指定ActiveMQ的网络监听端口，则默认端口都将使用BIO网络IO模型

**代码采用NIO**

## 步骤

1. 修改配置文件activemq.xml，因为activemq默认不支持NIO，所以需要手动配置

   ```xml
   <transportConnector name="nio" uri="nio://0.0.0.0:61618?trace=true"/>
   ```

2. 在java里直接更换uri即可



## 让默认端口既支持NIO，又支持多个协议的步骤

1. 开启auto传输前缀
2. 使用+符号为端口设置多种特性

```xml
<transportConnector name="auto+nio" uri="auto+nio://0.0.0.0:61608?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600&amp;org.apache.activemq.transport.nio.SelectorManager.corePoolSize=20&amp;org.apache.activemq.transport.nio.SelectorManager.maximumPoolSize=50" />
```



## TCP

* 默认的Broker配置，TCP的client监控端口是**61616**
* 在网络传输数据前，必须要序列化数据，消息是通过一个叫`wire protocol`的来序列化成字节流。默认情况下ActiveMQ把`wire protocol`叫做`OpenWire`，它的目的是促使网络上的效率和数据快速交互
* TCP的连接URI形式如： tcp://hostname:port?key=value&key=value，后面参数可选
* TCP传输的优点
  1. 可靠性高，稳定性强
  2. 高效性：字节流方式传递，效率很高
  3. 有效性、可用性：应用广泛，支持任何平台
* [详细配置入口](https://activemq.apache.org/tcp-transport-reference)

## NIO

* NIO和TCP协议类似，但更侧重于低层的访问操作。它允许开发人员对同一资源可有更多的client调用和服务器有更多的负载
* 适合场景
  1. 可能有大量的Client去连接到Broker上。一般情况下，大量的Client去连接Broker是被操作系统的线程所限制的，因此，NIO的实现比TCP需要更少的线程去运行
  2. 可能对于Broker有一个很迟钝的网络运输，NIO比TCP提供更好的性能
* URI形式：nio//hostname:port?key=value
* [详细配置入口](https://activemq.apache.org/nio-transport-reference)

## AMQP（Advanced Message Queuing Protocol）

提供统一消息服务的应用层标准高级消息队列协议，是应用层协议的一个开放标准，为面向消息的中间件设计。

基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同开发语言等条件的限制

## STOMP（Streaming Text orientated Message Protocol）

流文本定向消息协议，是一种以MOM(消息中间件)设计的简单文本协议

## SSL（Secure Sockets Layer）

安全套接字协议，是为网络通信提供安全及数据完整性的一种安全协议

## MQTT（Message Queuing Telemetry Transport）

消息队列遥测传输，是IBM开发的一个即时通讯协议，有可能成为互联网的重要组成部分。

该协议支持所有平台，几乎可以把所有联网物品和外部连接起来，被用来当做传感器和制动器的通信协议