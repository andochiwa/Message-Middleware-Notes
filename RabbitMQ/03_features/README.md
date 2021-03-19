# TTL过期时间

> TTL表示可以对消息设置预期时间，这个时间内可以被消费者接收获取，过了之后将自动被删除。

RabbitMQ可以对消息和队列设置TTL，有两种方法可以设置

1. 通过队列属性设置，队列中所有消息都具有相同的过期时间
2. 对消息单独设置，每条消息的TTL可以不同

如果上述两种方法同时使用，则消息的过期时间以两者之间TTL较小的数值为准。

消息在队列的生存时间一旦超过TTL值，就会投递到死信队列(dmq)，消息者将无法再收到消息



# 死信队列

> DLX，全称Dead-Letter_Exchange，可以称之为死信交换机。当消息在一个队列中变成有毒消息后，它能被重新发送到另一个交换机中，这个交换机就是DLX，绑定DLX的队列就称之为死信队列

消息有毒可能是由于以下原因

* 消息被拒绝
* 消息过期
* 队列达到最大长度

DLX也是一个正常的交换机，和一般的交换机没有区别，它能在任何的队列上被指定，实际上就是设置某一个队列的属性。当这个队列中存在有毒消息时，RabbitMQ就会自动地将这个消息重新发布到设置DLX上，进而被路由到另一个队列，即为死信队列

想要使用死信队列，只需要在定义队列时设置队列参数`x-dead-letter-exchange`指定交换机即可



# 内存警告与控制

> 当内存使用超过配置的阈值或者磁盘剩余空间小于配置的阈值时，RabbitMQ会暂时阻塞客户端的连接，并且停止接收从客户端发来的消息，以此避免服务器的崩溃，客户端与服务端的心跳检测机制也会失效

[配置文档](https://www.rabbitmq.com/configure.html)

## 命令的方式

```bash
rabbitmqctl set_vm_memory_high_watermark <fraction>
rabbitmqctl set_vm_memory_high_watermark absolute xxx
```

两者选其一，`fraction/value`为内存阈值，默认情况是0.4/2GB，代表当RabbitMQ的内存超过40%时，就会产生警告并阻塞所有生产者的连接。通过命令修改的阈值在broker重启后会失效

## 配置文件方式

在配置文件`/etc/rabbitmq/rabbitmq.conf`中配置

* `vm_memory_high_watermark.relative` 相对大小
* `vm_memory_high_watermark.absolute` 绝对大小

# 内存换页

> 在某个Broker节点及内存阻塞生产者前，它会尝试将队列中的消息换页到磁盘以释放内存空间，持久化和非持久化的消息都会写入磁盘中，其中持久化的消息本身就在磁盘中有一个副本，所以在转移的过程中持久化的消息会先从内存中清除掉

比如有1000MB内存，当内存使用率达到了400MB极限，如果配置了换页内存0.5，则会到达400MB极限之前，把内存中的200MB进行转移到磁盘中，从而达到稳健的运行

`vm_memory_high_watermark_paging_ratio`来调整

# 磁盘预警

> 当磁盘的剩余空间低于确定的阈值时，RabbitMQ同样会阻塞生产者，这样可以避免因非持久化的消息持续换页而耗尽磁盘空间导致服务器崩溃

* `disk_free_limit_relative` 相对大小
* `disk_free_limit_absolute` 绝对大小