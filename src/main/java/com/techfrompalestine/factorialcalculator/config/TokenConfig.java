package com.techfrompalestine.factorialcalculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration class responsible for connection validation across different environments.
 * 
 * This class maintains environment-specific connection tokens and provides validation logic
 * to ensure secure access to the application based on the current environment.
 */
@Configuration
public class TokenConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenConfig.class);
    
    // Supported environments
    private static final Set<String> SUPPORTED_ENVIRONMENTS = Set.of("dev", "tst", "acc", "prod");
    
    // Base64 encoded tokens for each environment
    private static final Map<String, String> ENVIRONMENT_TOKENS = Map.of(
        "dev", "dGVzdC10b2tlbi0xMjM=",   // test-token-123
        "tst", "dGVzdC10b2tlbi10ZXN0",   // test-token-test
        "acc", "dGVzdC10b2tlbi1hY2M=",   // test-token-acc
        "prod", "dGVzdC10b2tlbi1wcm9k"  // test-token-prod
    );
    
    @Value("${app.token}")
    private String configuredToken;
    
    @Value("${app.env}")
    private String currentEnvironment;
    
    private final Map<String, String> decodedTokenCache = new ConcurrentHashMap<>();
    
    /**
     * Initializes the configuration and validates the environment setup.
     * Called automatically after bean construction.
     */
    @PostConstruct
    public void initialize() {
        validateEnvironmentConfiguration();
        preloadTokenCache();
        logConfigurationStatus();
    }
    
    /**
     * Validates that the current environment is supported.
     * 
     * @throws IllegalStateException if environment is not supported
     */
    private void validateEnvironmentConfiguration() {
        if (!SUPPORTED_ENVIRONMENTS.contains(currentEnvironment)) {
            String supportedEnvs = String.join(", ", SUPPORTED_ENVIRONMENTS);
            throw new IllegalStateException(
                String.format("Invalid environment '%s'. Supported environments: %s", 
                            currentEnvironment, supportedEnvs)
            );
        }
    }
    
    /**
     * Pre-loads decoded tokens into cache for better performance.
     */
    private void preloadTokenCache() {
        ENVIRONMENT_TOKENS.forEach((env, encodedToken) -> {
            try {
                String decodedToken = decodeToken(encodedToken);
                decodedTokenCache.put(env, decodedToken);
                logger.debug("Cached token for environment: {}", env);
            } catch (Exception e) {
                logger.error("Failed to decode token for environment '{}': {}", env, e.getMessage());
            }
        });
    }
    
    /**
     * Logs the current configuration status.
     */
    private void logConfigurationStatus() {
        logger.info("=== Connection Configuration Initialized ===");
        logger.info("Current Environment: '{}'", currentEnvironment);
        logger.info("Supported Environments: {}", SUPPORTED_ENVIRONMENTS);
        logger.info("Connection validation ready");
        logger.info("=======================================");
    }
    
    /**
     * Validates the configured token against the expected token for the current environment.
     * 
     * @return true if the connection is valid, false otherwise
     */
    public boolean isTokenValid() {
        try {
            String expectedToken = getExpectedTokenForCurrentEnvironment();
            String actualToken = normalizeToken(configuredToken);
            
            boolean isValid = expectedToken.equals(actualToken);
            
            logger.debug("Connection validation for environment '{}': {}", currentEnvironment, 
                        isValid ? "VALID" : "INVALID");
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("Connection validation failed for environment '{}': {}", 
                        currentEnvironment, e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the expected token for the current environment.
     * 
     * @return The decoded expected token
     * @throws IllegalStateException if no token is configured for the current environment
     */
    private String getExpectedTokenForCurrentEnvironment() {
        String expectedToken = decodedTokenCache.get(currentEnvironment);
        
        if (expectedToken == null) {
            throw new IllegalStateException(
                String.format("No token configured for environment: %s", currentEnvironment)
            );
        }
        
        return expectedToken;
    }
    
    /**
     * Normalizes a token by trimming whitespace and handling null values.
     * 
     * @param token The token to normalize
     * @return The normalized token, or empty string if token is null
     */
    private String normalizeToken(String token) {
        return token != null ? token.trim() : "";
    }
    
    /**
     * Decodes a Base64 encoded token.
     * 
     * @param encodedToken The Base64 encoded token
     * @return The decoded token string
     * @throws IllegalArgumentException if the token cannot be decoded
     */
    private String decodeToken(String encodedToken) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Failed to decode token: %s", e.getMessage()), e
            );
        }
    }
    
    // Getters for accessing configuration values (useful for testing and debugging)
    
    /**
     * Gets the configured token.
     * 
     * @return The configured token string
     */
    public String getConfiguredToken() {
        return configuredToken;
    }
    
    /**
     * Gets the current environment.
     * 
     * @return The current environment name
     */
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    /**
     * Gets a copy of the environment tokens map.
     * 
     * @return Immutable map of environment tokens
     */
    public Map<String, String> getEnvironmentTokens() {
        return Map.copyOf(ENVIRONMENT_TOKENS);
    }
    
    /**
     * Gets the set of supported environments.
     * 
     * @return Immutable set of supported environment names
     */
    public Set<String> getSupportedEnvironments() {
        return Set.copyOf(SUPPORTED_ENVIRONMENTS);
    }
    
    /**
     * Checks if a given environment is supported.
     * 
     * @param environment The environment to check
     * @return true if the environment is supported, false otherwise
     */
    public boolean isEnvironmentSupported(String environment) {
        return SUPPORTED_ENVIRONMENTS.contains(environment);
    }
} 