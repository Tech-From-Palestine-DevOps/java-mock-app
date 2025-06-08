package com.example.tokenvalidator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Service responsible for adding common model attributes to views.
 * Centralizes the management of application-wide template variables.
 */
@Service
public class ModelAttributeService {
    
    // Application configuration properties
    @Value("${app.name}")
    private String applicationName;
    
    @Value("${app.env}")
    private String applicationEnvironment;
    
    @Value("${app.author}")
    private String applicationAuthor;
    
    @Value("${app.version}")
    private String applicationVersion;
    
    @Value("${app.resources.cpu:1}")
    private int cpuResources;
    
    // Attribute name constants for consistency
    private static final String APP_NAME_ATTR = "appName";
    private static final String APP_ENV_ATTR = "appEnv";
    private static final String APP_AUTHOR_ATTR = "appAuthor";
    private static final String APP_VERSION_ATTR = "appVersion";
    private static final String APP_RESOURCES_CPU_ATTR = "appResourcesCpu";
    private static final String AVERAGE_RESPONSE_TIME_ATTR = "averageResponseTime";
    private static final String SAMPLE_COUNT_ATTR = "sampleCount";
    
    /**
     * Adds common application attributes to the model.
     * These attributes are used across multiple views.
     */
    public void addApplicationAttributes(Model model) {
        model.addAttribute(APP_NAME_ATTR, applicationName);
        model.addAttribute(APP_ENV_ATTR, applicationEnvironment);
        model.addAttribute(APP_AUTHOR_ATTR, applicationAuthor);
        model.addAttribute(APP_VERSION_ATTR, applicationVersion);
    }
    
    /**
     * Adds specific attributes needed for the properties page.
     * Includes CPU resources and response time statistics.
     */
    public void addPropertiesPageAttributes(Model model, ResponseTimeService responseTimeService) {
        model.addAttribute(APP_RESOURCES_CPU_ATTR, cpuResources);
        model.addAttribute(AVERAGE_RESPONSE_TIME_ATTR, 
                          String.format("%.3f", responseTimeService.getAverageResponseTime()));
        model.addAttribute(SAMPLE_COUNT_ATTR, responseTimeService.getSampleCount());
    }
    
    // Getters for testing purposes
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getApplicationEnvironment() {
        return applicationEnvironment;
    }
    
    public String getApplicationAuthor() {
        return applicationAuthor;
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    public int getCpuResources() {
        return cpuResources;
    }
} 