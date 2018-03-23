### rocketmq的基础集成，项目中如果有需要，直接引入即可
### 其他项目引用步骤：
#### 1、pom.xml中引入common-rocketmq-service依赖坐标
```
<!-- 引入common-rocketmq-service(封装了rocketMq基础调用) -->
<dependency>
	<groupId>com.feizi</groupId>
	<artifactId>common-rocketmq-service</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```
#### 2、配置消费者监听器和相应的topic以及消费者组信息,例如：
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 注入消费者(订单数据接收)监听器 -->
    <bean id="orderConsumerListener"  class="com.feizi.dubbo.rocketmq.consumer.OrderConsumerListener"/>

    <!-- 流程任务Topic -->
    <bean id="createTaskTopicConsumer" class="com.feizi.rocketmq.common.service.rocketmq.MQConsumer" init-method="start" destroy-method="shutdown">
        <!-- TODO: 配置消费者组 -->
        <constructor-arg value="order_consumer_group" index="0" />
        <!-- TODO: 设置消费topic -->
        <constructor-arg value="receive_order_data_topic" index="1" />
        <!-- topic's subExpression -->
        <constructor-arg value="*" index="2" />
        <!-- 设置从队列头部开始消费 -->
        <property name="consumeFromWhere" ref="CONSUME_FROM_FIRST_OFFSET" />
        <!-- 配置消费者监听器 -->
        <property name="consumeListener" ref="orderConsumerListener"/>
        <!--集群模式，默认为集群消费，不需要配置 -->
        <!--<property name="messageModel" ref="CLUSTERING"/> -->
    </bean>

    <!-- 引入基础MQ配置 -->
    <import resource="classpath*:config/common/service/common-service.xml" />
</beans>
```
