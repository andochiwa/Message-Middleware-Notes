# 队列

一对一关系，一个生产者发送消息由一个消费者接收



# 队列多个消费者的情况

默认采用轮询机制



# 主题

一对多关系，一个生产者发送消息所有消费者都会接收

**先要启动订阅再启动生产，否则发送的消息无效**