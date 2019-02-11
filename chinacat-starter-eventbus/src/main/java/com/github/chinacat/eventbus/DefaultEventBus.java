package com.github.chinacat.eventbus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chinacat.eventbus.config.MqConfig;
import com.github.chinacat.eventbus.exception.PublishException;
import com.github.chinacat.eventbus.model.Event;
import com.github.chinacat.eventbus.model.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @author s.c.gao
 */
public class DefaultEventBus implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventBus.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Override
    public Object publish(Event event) throws PublishException {
        try {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(UUID.randomUUID().toString());
            amqpTemplate.convertAndSend(
                    MqConfig.EVENT_EXCHANGE,
                    MqConfig.EVENT_ROUTING_KEY,
                    MAPPER.writeValueAsString(event),
                    correlationData);
        } catch (JsonProcessingException e) {
            log.error("转发消息编码错误", e);
            throw new PublishException();
        } catch (Throwable t) {
            log.error("未知异常", t);
            throw new PublishException();
        }
        return event;
    }
}
