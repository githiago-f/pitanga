package br.edu.ifrs.poa.pitanga_code.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "pitanga.sandbox.files")
public class FilesConfiguration {
    private String stdin;
    private String stdout;
    private String stderr;

    public String[] getFiles() {
        return new String[] { stdin, stderr, stdout };
    }
}
