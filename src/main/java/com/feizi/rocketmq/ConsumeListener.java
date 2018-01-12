package com.feizi.rocketmq;

import com.alibaba.rocketmq.common.message.Message;

/**
 * 消费监听器
 * Created by feizi on 2018/1/12.
 */
public interface ConsumeListener {

    /**
     * 消费消息
     * @param message MQ消息
     * @return
     */
    boolean consume(Message message);
}
