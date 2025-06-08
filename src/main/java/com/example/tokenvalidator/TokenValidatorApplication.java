package com.example.tokenvalidator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpringBootApplication
public class TokenValidatorApplication {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setFileEncoding(StandardCharsets.UTF_8.name());
        configurer.setIgnoreResourceNotFound(true);
        configurer.setIgnoreUnresolvablePlaceholders(true);
        
        Resource resource = new ClassPathResource("application.properties");
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            configurer.setProperties(props);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        
        return configurer;
    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
        SpringApplication.run(TokenValidatorApplication.class, args);
    }
} 