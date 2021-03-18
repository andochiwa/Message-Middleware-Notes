# 整合spring
内部继承类的Rabbit工具类：`RabbitTemplate`

在test中发布 

在主程序中消费

## @RabbitListener

表明和队列绑定的信息

* `queues` 结合配置类使用，指定队列名

不用配置类方法（不推荐？）

```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "队列名", durable = "true", autoDelete = "false"),
    exchange = @Exchange(value = "路由名", type = "路由type：ExchangeTypes.TOPIC"),
    key = "路由key"
))
```



## @RabbitHandler

告知接受消息的方法（落脚点）