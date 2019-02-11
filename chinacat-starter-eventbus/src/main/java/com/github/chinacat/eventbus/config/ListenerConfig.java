package com.github.chinacat.eventbus.config;

import com.github.chinacat.eventbus.AbstractEventListener;
import com.github.chinacat.eventbus.config.property.MqProperties;
import com.github.chinacat.eventbus.model.EventListener;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author s.c.gao
 */
@Configuration
public class ListenerConfig {

    @Autowired
    private MqProperties mqProperties;

    @Bean
    public SimpleMessageListenerContainer mqMessageContainer(@Autowired ConnectionFactory connectionFactory,
                                                             @Autowired EventListener eventListener) throws AmqpException {
        if (Objects.nonNull(mqProperties.getQueue())) {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
            container.setQueueNames(mqProperties.getQueue());
            container.setExposeListenerChannel(true);
            //设置每个消费者获取的最大的消息数量
            container.setPrefetchCount(1);
            //消费者个数
            container.setConcurrentConsumers(1);
            //设置确认模式为手工确认
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
            //监听处理类
            container.setMessageListener(eventListener);
            return container;
        } else {
            return null;
        }
    }

    @Bean
    public AbstractEventListener.MessageAck messageAck() {
        return new AbstractEventListener.MessageAck();
    }
}
