package com.github.chinacat.eventbus.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * @author s.c.gao
 */
@ConfigurationProperties(prefix = MqProperties.PREFIX)
@Data
public class MqProperties {

    public static final String PREFIX = "chinacat.event";

    private String type;

    public String getQueue() {
        if (Objects.isNull(type) || type.isBlank()) {
            return null;
        } else {
            return type + "-queue";
        }
    }
}
