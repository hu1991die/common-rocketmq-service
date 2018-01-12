package com.feizi.rocketmq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQ消息生产者实现
 * Created by feizi on 2018/1/12.
 */
public class MQProducerImpl extends DefaultMQProducer implements MQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQProducerImpl.class);

    public MQProducerImpl() {
    }

    public MQProducerImpl(String producerGroup) {
        super(producerGroup);
    }

    @Override
    public void start() throws MQClientException {
        super.start();
    }

    /**
     * 发送MQ消息
     * @param message MQ消息
     * @return
     */
    @Override
    public SendResult sendMessage(Message message) {
        try {
            return send(message);
        } catch (Exception e) {
            LOGGER.error("Send message error...", e);
            return null;
        }
    }

    /**
     * 发送异步MQ消息
     * @param message MQ消息
     * @param callback 发送回调
     */
    @Override
    public void sendAsyncMessage(Message message, final IMessageCallback callback) {
        try {
            send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    callback.callBack(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    LOGGER.error("Send asyncMessage callback error...", e);
                }
            });
        }catch (Exception e) {
            LOGGER.error("Send asyncMessage callback error...", e);
        }
    }

    /**
     * 发送异步MQ消息
     * @param message MQ消息
     */
    @Override
    public void sendAsyncMessage(Message message) {
        sendAsyncMessage(message, new IMessageCallback() {
            @Override
            public void callBack(SendResult result) {
                if(LOGGER.isInfoEnabled()){
                    LOGGER.info("Send asyncMessage success...The message is: " + result.toString());
                }
            }
        });
    }

    /**
     * 发送MQ消息，如果出现失败进行重试
     * @param message MQ消息
     * @param recount 重试次数
     * @return 是否发送成功
     */
    @Override
    public boolean sendMessage(Message message, int recount) {
        boolean result = false;
        int count = 0;
        while (count < recount){
            SendResult sendResult = sendMessage(message);
            if(null == sendResult){
                //发送失败，继续重试
                count++;
            }else {
                LOGGER.info("Send message over, the result is: " + sendResult.getSendStatus());
                if(SendStatus.SEND_OK == sendResult.getSendStatus()){
                    //发送成功，设置标识
                    result = true;
                    break;
                }else {
                    //如果发送失败， 继续重试
                    count++;
                }
            }
        }
        return result;
    }
}
