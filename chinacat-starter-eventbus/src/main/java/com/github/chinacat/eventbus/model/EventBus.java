package com.github.chinacat.eventbus.model;


import com.github.chinacat.eventbus.exception.PublishException;

/**
 * @author s.c.gao
 */
public interface EventBus {

    /**
     * 发布事件
     *
     * @param event 待发布的事件
     * @return Object 发布后的任意信息
     * @throws PublishException 发布异常
     */
    Object publish(Event event) throws PublishException;

}
