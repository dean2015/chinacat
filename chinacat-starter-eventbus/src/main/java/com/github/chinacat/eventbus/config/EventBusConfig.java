package com.github.chinacat.eventbus.config;

import com.github.chinacat.eventbus.DefaultEventBus;
import com.github.chinacat.eventbus.model.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {

    @Bean("remoteEventBus")
    public EventBus eventBus(){
        return new DefaultEventBus();
    }

}
