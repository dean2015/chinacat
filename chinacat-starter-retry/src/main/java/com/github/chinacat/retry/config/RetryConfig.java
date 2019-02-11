package com.github.chinacat.retry.config;

import com.github.chinacat.retry.RetryAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author s.c.gao
 */
@Configuration
public class RetryConfig {

    @Bean
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }
}
