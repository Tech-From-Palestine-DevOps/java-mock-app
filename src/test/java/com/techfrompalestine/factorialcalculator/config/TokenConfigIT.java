package com.techfrompalestine.factorialcalculator.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenConfigIT {

    @Value("${app.token}")
    private String applicationToken;

    @Value("${app.env}")
    private String applicationEnv;

    @Autowired
    private TokenConfig tokenConfig;

    @Test
    void testTokenValidation() {
        // Get the token for the current environment
        String validTokenForEnv = tokenConfig.getEnvironmentTokens().get(applicationEnv);
        assertNotNull(validTokenForEnv, "No token configured for environment: " + applicationEnv);
        
        // Get the decoded value
        String decodedValidToken = new String(java.util.Base64.getDecoder().decode(validTokenForEnv));
        
        // Compare the application.properties token with the decoded token
        boolean isValid = decodedValidToken.equals(applicationToken);
        
        System.out.println("Environment: " + applicationEnv);
        System.out.println("Token from application.properties: " + applicationToken);
        System.out.println("Decoded token from TokenConfig: " + decodedValidToken);
        System.out.println("Are they equal? " + isValid);
        
        assertTrue(isValid, "Token validation should fail when tokens don't match");
    }
} 