package com.tracing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "tracing")
public class TracingProperties {
    private int timeInterval;
    private int timeLimit;
}
