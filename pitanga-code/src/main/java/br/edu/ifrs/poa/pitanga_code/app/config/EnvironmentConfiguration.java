package br.edu.ifrs.poa.pitanga_code.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pitanga.sandbox.environment")
public class EnvironmentConfiguration {
    private String defaultPath;
    private Boolean useCgroups;
}
