package br.edu.ifrs.poa.pitanga_code.app.config;

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
    private Integer fileSize;
    private Integer openFiles;
    private Integer processesAndThreads;

    private Double maxCpuTime;
    private Double maxExtraCpuTime;
    private Double maxWallTime;
    private Integer maxMemory;
    private Integer maxStack;
}
