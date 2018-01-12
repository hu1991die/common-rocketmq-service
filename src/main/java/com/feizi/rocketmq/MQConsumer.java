package com.feizi.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * MQ消费者
 * Created by feizi on 2018/1/12.
 */
public class MQConsumer extends DefaultMQPushConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumer.class);

    @Override
    public void start() throws MQClientException {
        /**
         * TODO just for test
         */
//        super.setNamesrvAddr("127.0.0.1:9876");
        super.start();
    }

    /**
     * MQ消费监听
     * @param consumeListener 消息监听器
     */
    public void setConsumeListener(final ConsumeListener consumeListener){
        super.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    MessageExt messageExt = msgs.get(0);
                    if(consumeListener.consume(messageExt)){
                        //返回消费成功
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }else {
                        //返回消费失败，需要进行重试
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                } catch (Exception e) {
                    //如果消费过程中出现异常，返回消费失败，需要进行重试
                    LOGGER.error("Consume message error...", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });
    }

    /**
     * 订阅Topic主题，指定消费者组
     * @param consumerGroup 消费者组
     * @param topic 消息主题Topic
     * @param subExpression 表达式
     */
    public MQConsumer(final String consumerGroup, final String topic, final String subExpression){
        super(consumerGroup);
        try {
            super.subscribe(topic, subExpression);
        } catch (MQClientException e) {
            LOGGER.error("Subcribe topic failure...", e);
        }
    }
}
