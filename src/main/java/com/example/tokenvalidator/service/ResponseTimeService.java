package com.example.tokenvalidator.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

/**
 * Service responsible for tracking and calculating response time statistics.
 * Maintains a rolling window of response times for performance monitoring.
 * 
 * This service is thread-safe and can handle concurrent requests.
 */
@Service
public class ResponseTimeService {
    
    private static final Logger logger = LoggerFactory.getLogger(ResponseTimeService.class);
    
    // Configuration constants
    private static final int DEFAULT_MAX_SAMPLES = 100;
    private static final double MINIMUM_RESPONSE_TIME = 0.0;
    
    private final Queue<Double> responseTimes;
    private final int maxSamples;
    
    /**
     * Constructor with default maximum samples configuration.
     */
    public ResponseTimeService() {
        this(DEFAULT_MAX_SAMPLES);
    }
    
    /**
     * Constructor allowing custom maximum samples configuration.
     * 
     * @param maxSamples Maximum number of response time samples to maintain
     */
    public ResponseTimeService(int maxSamples) {
        this.responseTimes = new ConcurrentLinkedQueue<>();
        this.maxSamples = Math.max(1, maxSamples); // Ensure at least 1 sample
        
        logger.info("ResponseTimeService initialized with max samples: {}", this.maxSamples);
    }
    
    /**
     * Records a new response time measurement.
     * If the maximum number of samples is exceeded, the oldest sample is removed.
     * 
     * @param responseTime The response time to record (in seconds)
     * @throws IllegalArgumentException if responseTime is negative
     */
    public void addResponseTime(double responseTime) {
        validateResponseTime(responseTime);
        
        synchronized (this) {
            responseTimes.offer(responseTime);
            
            // Remove oldest samples if we exceed the maximum
            while (responseTimes.size() > maxSamples) {
                responseTimes.poll();
            }
        }
        
        logger.debug("Recorded response time: {:.3f}s (total samples: {})", 
                    responseTime, responseTimes.size());
    }
    
    /**
     * Calculates the average response time from all recorded samples.
     * 
     * @return The average response time in seconds, or 0.0 if no samples exist
     */
    public double getAverageResponseTime() {
        if (responseTimes.isEmpty()) {
            return 0.0;
        }
        
        synchronized (this) {
            double sum = responseTimes.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
            
            double average = sum / responseTimes.size();
            
            logger.debug("Calculated average response time: {:.3f}s from {} samples", 
                        average, responseTimes.size());
            
            return average;
        }
    }
    
    /**
     * Gets the current number of response time samples.
     * 
     * @return The number of recorded samples
     */
    public int getSampleCount() {
        return responseTimes.size();
    }
    
    /**
     * Gets the maximum response time from recorded samples.
     * 
     * @return The maximum response time, or 0.0 if no samples exist
     */
    public double getMaxResponseTime() {
        if (responseTimes.isEmpty()) {
            return 0.0;
        }
        
        synchronized (this) {
            return responseTimes.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);
        }
    }
    
    /**
     * Gets the minimum response time from recorded samples.
     * 
     * @return The minimum response time, or 0.0 if no samples exist
     */
    public double getMinResponseTime() {
        if (responseTimes.isEmpty()) {
            return 0.0;
        }
        
        synchronized (this) {
            return responseTimes.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
        }
    }
    
    /**
     * Clears all recorded response time samples.
     * Useful for resetting statistics.
     */
    public void clearSamples() {
        synchronized (this) {
            responseTimes.clear();
            logger.info("Cleared all response time samples");
        }
    }
    
    /**
     * Gets the configured maximum number of samples.
     * 
     * @return The maximum sample count
     */
    public int getMaxSamples() {
        return maxSamples;
    }
    
    /**
     * Checks if the service has any recorded samples.
     * 
     * @return true if samples exist, false otherwise
     */
    public boolean hasSamples() {
        return !responseTimes.isEmpty();
    }
    
    /**
     * Validates that a response time value is acceptable.
     * 
     * @param responseTime The response time to validate
     * @throws IllegalArgumentException if responseTime is invalid
     */
    private void validateResponseTime(double responseTime) {
        if (responseTime < MINIMUM_RESPONSE_TIME) {
            throw new IllegalArgumentException(
                String.format("Response time cannot be negative: %.3f", responseTime)
            );
        }
        
        if (Double.isNaN(responseTime) || Double.isInfinite(responseTime)) {
            throw new IllegalArgumentException(
                String.format("Response time must be a valid number: %.3f", responseTime)
            );
        }
    }
} 