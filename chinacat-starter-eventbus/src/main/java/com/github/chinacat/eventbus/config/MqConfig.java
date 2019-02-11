package com.github.chinacat.eventbus.config;

import com.github.chinacat.eventbus.MessageReturnCallback;
import com.github.chinacat.eventbus.MessageSendConfirm;
import com.github.chinacat.eventbus.config.property.MqProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author s.c.gao
 */
@Configuration
public class MqConfig {

    public static final String EVENT_EXCHANGE = "event-exchange";

    @Bean
    TopicExchange eventExchange() {
        return new TopicExchange(EVENT_EXCHANGE);
    }

    public static final String EVENT_ROUTING_KEY = "event.routing.key.#";

    @Autowired
    MqProperties mqProperties;

    @Bean
    Queue eventQueue() {
        if (Objects.nonNull(mqProperties.getQueue())) {
            return new Queue(mqProperties.getQueue(), true, false, false);
        } else {
            return null;
        }
    }

    @Bean
    Binding appTaskQueueBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with(EVENT_ROUTING_KEY);
    }

    @Bean
    MessageReturnCallback messageReturnCallback() {
        return new MessageReturnCallback();
    }

    @Bean
    MessageSendConfirm messageSendConfirm() {
        return new MessageSendConfirm();
    }

}
