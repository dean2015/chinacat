package com.github.chinacat.eventbus;


import com.github.chinacat.eventbus.model.Event;

/**
 * @author s.c.gao
 */
public class DefaultEvent<T> implements Event<T> {

    private String eventType;

    private T body;

    public DefaultEvent() {
    }

    public DefaultEvent(T body) {
        this.body = body;
    }

    @Override
    public final String getEventType() {
        if (eventType == null) {
            eventType = this.body.getClass().getName();
        }
        return eventType;
    }

    @Override
    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
