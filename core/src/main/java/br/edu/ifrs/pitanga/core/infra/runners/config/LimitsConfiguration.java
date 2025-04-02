package br.edu.ifrs.pitanga.core.infra.runners.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pitanga.sandbox.limits")
public class LimitsConfiguration {
    private Long retry;
    private Long retryMilis;

    private Double cpuTime;
    private Double extraCpuTime;
    private Double wallTime;
    private Integer memory;
    private Integer stack;

    private Double maxCpuTime;
    private Double maxExtraCpuTime;
    private Double maxWallTime;
    private Integer maxMemory;
    private Integer maxStack;

    public Duration getRetryDuration() {
        return Duration.ofMillis(retryMilis);
    }
}
