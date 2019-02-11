package com.github.chinacat.eventbus;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chinacat.eventbus.model.Event;
import com.github.chinacat.eventbus.model.EventListener;
import com.github.chinacat.retry.Retry;
import com.github.chinacat.retry.RetryTriggerException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author s.c.gao
 */
@Slf4j
public abstract class AbstractEventListener<T extends Serializable> implements EventListener<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MessageAck messageAck;

    private Class<T> clazz;

    public AbstractEventListener(Class<T> elementClass) {
        clazz = elementClass;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        Event<T> event = convert(message.getBody(), getGenericType(clazz));
        if (Objects.isNull(event)) {
            log.error("消息转换错误");
            try {
                messageAck.basicNack(message, channel);
            } catch (RetryTriggerException e) {
                log.error("消息拒绝并放回队列操作了多次但是不能成功，这里将直接返回，等通道失效时消息将自动回到队列");
            }
            return;
        }
        if (!event.getEventType().equalsIgnoreCase(clazz.getName())) {
            log.error("不是当前服务关注的事件处理类型");
            try {
                messageAck.basicNack(message, channel);
            } catch (RetryTriggerException e) {
                log.error("消息拒绝并放回队列操作了多次但是不能成功，这里将直接返回，等通道失效时消息将自动回到队列");
            }
            return;
        }
        try {
            handle(event);
        } catch (Exception e) {
            log.error("", e);
            try {
                messageAck.basicNack(message, channel);
            } catch (RetryTriggerException rte) {
                log.error("消息拒绝并放回队列操作了多次但是不能成功，这里将直接返回，等通道失效时消息将自动回到队列", rte);
            }
            return;
        }

        try {
            messageAck.basicAck(message, channel);
        } catch (RetryTriggerException e) {
            log.error("消息确认已经重试了多次，无法进行确认，目前消息已经被处理，无法让从消息队列中移除该消息。" +
                    "当你看到这条信息的时候，说明消息队列已经和当前服务不在一个网络环境中，请在消息队列中移除当前已经处理过的消息后，" +
                    "再次尝试将消息队列和当前服务加入到同一个网络环境中");
        }
    }

    /**
     * 消息确认类
     *
     * @author s.c.gao
     */
    public static class MessageAck {

        @Retry(times = 7)
        private void basicNack(Message message, Channel channel) throws RetryTriggerException {
            long tag = message.getMessageProperties().getDeliveryTag();
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException e) {
                log.error("消息拒绝并重新放回队列异常", e);
                throw new RetryTriggerException();
            }
        }

        @Retry(times = 7)
        private void basicAck(Message message, Channel channel) throws RetryTriggerException {
            long tag = message.getMessageProperties().getDeliveryTag();
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                log.error("消息确认异常", e);
                throw new RetryTriggerException();
            }
        }
    }

    private static JavaType getGenericType(Class<?> elementClass) {
        return MAPPER.getTypeFactory().constructParametricType(DefaultEvent.class, elementClass);
    }

    private static <E> E convert(byte[] valus, JavaType type) {
        try {
            return MAPPER.readValue(valus, type);
        } catch (IOException e) {
            log.error("An IOException happened during message converting.", e);
            return null;
        }
    }
}
