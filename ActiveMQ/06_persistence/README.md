# ActiveMQ持久化机制

> 持久化：高可用的一种保障，哪怕MQ的服务器宕机了，消息也不会丢失
>
> 为了避免服务器意外宕机，需要做到重启后可以恢复消息队列，消息系统一般都会采用持久化机制

ActiveMQ的持久化机制有`JDBC`, `AMQ`, `KahaDB`, `LevelDB`, 无论采用哪种机制，消息的存储逻辑都是一致的。

发送者将消息发送出去后，消息中心首先将消息存储到本地数据文件，内存数据库或者远程数据库等，试图将消息发送给接收者，**成功则将消息从存储中删除**，失败则继续尝试发送

消息中心启动以后首先要检查指定的**存储位置**，如果有未发送成功的信息，则需要把信息发送出去



## KahaDB（默认）

基于日志文件，从ActiveMQ5.4开始作为默认的持久化插件，可用于任何场景，提高了性能和恢复能力。

消息存储使用一个**事务日志**和一个**索引文件**来存储它所有的地址。

KahaDB是一个专门针对消息持久化的解决方案，它对典型的消息使用模式进行了优化

数据被追加到data logs中，当不再需要log文件中的数据时，log文件会被丢弃

## JDBC消息存储

使用步骤

* 导入数据库相关jar包到activemq的lib目录（包括但不限于mysql，druid包），修改配置文件

  ```xml
  <persistenceAdapter>
      <jdbcPersistenceAdapter dataSource="#mysql-ds" createTablesOnStartup="true"/>
  </persistenceAdapter>
  
  <bean id="mysql-ds" class="com.alibaba.druid.pool.DruidDataSource" >
      <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
      <property name="url" value="jdbc:mysql://localhost:3306/activemq?serverTimezone=Japan&amp;useSSL=false&amp;useUnicode=true&amp;characterEncoding=utf-8"/>
      <property name="username" value="root"/>
      <property name="password" value="root"/>
      <property name="initialSize" value="5" />
      <property name="minIdle" value="1" />
      <property name="maxActive" value="30"/>
      <property name="maxWait" value="60000"/>
  </bean>
  ```

  `createTablesOnStartup`，默认值为true，表示是否在启动时创建数据表，一般设置第一次为true后面为false

  `bean`标签要放在`broker`之外

* 在数据库中创建相应的库

* 发布消息后，消息会存储到表中，消费后，会从表中删除

存放表的位置

* `queue` 保存在`activemq_msgs`表中
* `topic` 保存在`activemq_acks`表中

## JDBC + Journal

> 克服了JDBC每次消息都需要去库里读写的缺点
>
> Journal使用高速缓存写入技术，让数据少或者不写到数据库中，大大的提升了性能

配置方法

* JDBC的步骤照搬，修改配置文件

  ```xml
  <persistenceFactory>
      <journalPersistenceAdapterFactory dataSource="#mysql-ds"
                                        dataDirectory="activemq-data"
                                        createTablesOnStartup="false"
                                        journalLogFiles="4"
                                        journalLogFileSize="32768"
                                        useJournal="true"
                                        useQuickJournal="true" />
  </persistenceFactory>
  ```

  

## AMQ Message Store

基于文件的存储方式，具有写入速度快和容器恢复的特点。消息存储在一个个文件中，默认大小为32M，当一个存储文件中的消息已经全部被消费，那么这个文件将被表示为可删除，在下一个清除阶段，这个文件被删除

适用于AMQ5.3以前的版本，现在基本不用了