package com.github.chinacat.eventbus.model;

import java.io.Serializable;

/**
 * @author s.c.gao
 */
public interface Event<T> extends Serializable {

    /**
     * 获取事件的类型， 例如body的类型
     * @return eventType
     */
    String getEventType();

    /**
     * 事件的主体信息
     * @return body
     */
    T getBody();

}
