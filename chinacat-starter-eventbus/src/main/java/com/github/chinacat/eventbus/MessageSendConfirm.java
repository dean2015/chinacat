package com.github.chinacat.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 消息发送，到达exchange时，这是使用这个类进行通知动作
 */
@Slf4j
public class MessageSendConfirm implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error(correlationData + " Exception cause: " + cause);
        } else {
            log.info("message to exchange : " + correlationData);
        }
    }

}
