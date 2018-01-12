package com.feizi.rocketmq;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * MQ消息生产者
 * Created by feizi on 2018/1/12.
 */
public interface MQProducer {

    /**
     * 发送MQ消息
     * @param message MQ消息
     * @return
     */
    SendResult sendMessage(Message message);

    /**
     * 发送异步MQ消息
     * @param message MQ消息
     * @param callback 发送回调
     */
    void sendAsyncMessage(Message message, IMessageCallback callback);

    /**
     * 发送异步MQ消息
     * @param message MQ消息
     */
    void sendAsyncMessage(Message message);

    /**
     * 发送MQ消息，如果出现失败进行重试
     * @param message MQ消息
     * @param recount 重试次数
     * @return 是否发送成功
     */
    boolean sendMessage(Message message, int recount);
}
