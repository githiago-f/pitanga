package br.edu.ifrs.pitanga.core.infra.runners;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pitanga.sandbox.limits")
public class LimitsConfiguration {
    private Double cpuTime;
    private Double extraCpuTime;
    private Double wallTime;
    private Integer memory;
    private Integer stack;
}
