package com.example.tokenvalidator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;

@Configuration
public class TokenConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenConfig.class);
    
    @Value("${app.token}")
    private String configuredToken;
    
    // Base64 encoded value of "test-token-123"
    public static final String VALID_TOKEN = "dGVzdC10b2tlbi0xMjM=";
    
    public String getConfiguredToken() {
        return configuredToken;
    }
    
    public boolean isTokenValid() {
        try {
            String decodedValidToken = new String(Base64.getDecoder().decode(VALID_TOKEN));
            String trimmedToken = configuredToken != null ? configuredToken.trim() : "";
            logger.info("Configured token: '{}'", trimmedToken);
            logger.info("Decoded valid token: '{}'", decodedValidToken);
            boolean isValid = decodedValidToken.equals(trimmedToken);
            logger.info("Token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Failed to decode token: {}", e.getMessage());
            return false;
        }
    }
} 