package com.github.chinacat.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


/**
 * 消息已经到达Exchange，但是在放入队列是出现问题，这是使用这个类进行通知动作
 */
@Slf4j
public class MessageReturnCallback implements RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setMandatory(true);
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体 message : " + message);
        log.info("消息返回码 replyCode : " + replyCode);
        log.info("描述：" + replyText);
        log.info("消息使用的交换器 exchange : " + exchange);
        log.info("消息使用的路由键 routingkey : " + routingKey);
    }
}
