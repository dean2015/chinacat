package com.github.chinacat.eventbus.model;

import com.github.chinacat.eventbus.exception.EventHandleException;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * @author s.c.gao
 */
public interface EventListener<T> extends ChannelAwareMessageListener {

    /**
     * 对事件的自定义处理
     *
     * @param event 事件
     * @return 暂时无用
     */
    Object handle(Event<T> event) throws EventHandleException;
}