package com.feizi.rocketmq;

import com.alibaba.rocketmq.client.producer.SendResult;

/**
 * 消息回调
 * Created by feizi on 2018/1/12.
 */
public interface IMessageCallback {

    /**
     * 发送完成进行回调
     * @param result
     */
    void callBack(SendResult result);
}
