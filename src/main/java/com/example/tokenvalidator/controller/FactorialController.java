package com.example.tokenvalidator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tokenvalidator.config.TokenConfig;
import com.example.tokenvalidator.service.CalculationService;
import com.example.tokenvalidator.service.ColorService;
import com.example.tokenvalidator.service.ResponseTimeService;
import com.example.tokenvalidator.service.ModelAttributeService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

/**
 * Main controller for handling factorial calculations and token validation.
 * Provides endpoints for the main page, properties page, and factorial calculations.
 */
@Controller
public class FactorialController {
    
    private static final Logger logger = LoggerFactory.getLogger(FactorialController.class);
    
    // Constants for better maintainability
    private static final String INDEX_VIEW = "index";
    private static final String PROPERTIES_VIEW = "properties";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String RESULT_ATTRIBUTE = "result";
    private static final String RESPONSE_TIME_ATTRIBUTE = "responseTime";
    private static final String NUMBER_ATTRIBUTE = "num1";
    private static final String IS_VALID_ATTRIBUTE = "isValid";
    
    private final TokenConfig tokenConfig;
    private final CalculationService calculationService;
    private final ResponseTimeService responseTimeService;
    private final ModelAttributeService modelAttributeService;
    private final Random random;
    
    @Value("${app.resources.cpu:1}")
    private int cpuResources;
    
    /**
     * Constructor injection for better testability and immutability
     */
    @Autowired
    public FactorialController(
            TokenConfig tokenConfig,
            CalculationService calculationService,
            ResponseTimeService responseTimeService,
            ModelAttributeService modelAttributeService) {
        this.tokenConfig = tokenConfig;
        this.calculationService = calculationService;
        this.responseTimeService = responseTimeService;
        this.modelAttributeService = modelAttributeService;
        this.random = new Random();
    }
    
    /**
     * Displays the main page with token validation status
     */
    @GetMapping("/")
    public String showMainPage(Model model) {
        boolean isTokenValid = tokenConfig.isTokenValid();
        
        modelAttributeService.addApplicationAttributes(model);
        model.addAttribute(IS_VALID_ATTRIBUTE, isTokenValid);
        
        return INDEX_VIEW;
    }
    
    /**
     * Displays the application properties page
     */
    @GetMapping("/properties")
    public String showPropertiesPage(Model model) {
        modelAttributeService.addApplicationAttributes(model);
        modelAttributeService.addPropertiesPageAttributes(model, responseTimeService);
        
        return PROPERTIES_VIEW;
    }
    
    /**
     * Handles factorial calculation requests
     */
    @PostMapping("/factorial")
    public String calculateFactorial(@RequestParam int number, Model model) {
        modelAttributeService.addApplicationAttributes(model);
        
        if (!isTokenValid()) {
            return handleInvalidToken(model);
        }
        
        try {
            FactorialResult result = performFactorialCalculation(number);
            return handleSuccessfulCalculation(model, number, result);
            
        } catch (InterruptedException e) {
            logger.error("Factorial calculation was interrupted for number: {}", number, e);
            Thread.currentThread().interrupt(); // Restore interrupted status
            return handleCalculationError(model, "Calculation was interrupted");
            
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid input for factorial calculation: {} - {}", number, e.getMessage());
            return handleCalculationError(model, e.getMessage());
        }
    }
    
    /**
     * Checks if the current token is valid
     */
    private boolean isTokenValid() {
        return tokenConfig.isTokenValid();
    }
    
    /**
     * Handles the case when token is invalid
     */
    private String handleInvalidToken(Model model) {
        model.addAttribute(ERROR_ATTRIBUTE, "Token invalid - Cannot perform calculation");
        model.addAttribute(IS_VALID_ATTRIBUTE, false);
        return INDEX_VIEW;
    }
    
    /**
     * Performs the factorial calculation with response time tracking
     */
    private FactorialResult performFactorialCalculation(int number) throws InterruptedException {
        double responseTime = calculateResponseTime();
        
        // Simulate processing time
        TimeUnit.MILLISECONDS.sleep((long)(responseTime * 1000));
        
        // Record response time for statistics
        responseTimeService.addResponseTime(responseTime);
        
        // Calculate factorial
        long factorialResult = calculationService.factorial(number);
        
        return new FactorialResult(factorialResult, responseTime);
    }
    
    /**
     * Calculates response time based on CPU resources and adds randomness
     */
    private double calculateResponseTime() {
        double baseTime = 2.0 / cpuResources;
        double randomJitter = random.nextDouble() * 0.5;
        double fixedOverhead = 0.01;
        
        return baseTime + randomJitter + fixedOverhead;
    }
    
    /**
     * Handles successful factorial calculation
     */
    private String handleSuccessfulCalculation(Model model, int number, FactorialResult result) {
        model.addAttribute(RESULT_ATTRIBUTE, result.getValue());
        model.addAttribute(RESPONSE_TIME_ATTRIBUTE, String.format("%.3f", result.getResponseTime()));
        model.addAttribute(NUMBER_ATTRIBUTE, number);
        model.addAttribute(IS_VALID_ATTRIBUTE, true);
        
        return INDEX_VIEW;
    }
    
    /**
     * Handles calculation errors
     */
    private String handleCalculationError(Model model, String errorMessage) {
        model.addAttribute(ERROR_ATTRIBUTE, errorMessage);
        model.addAttribute(IS_VALID_ATTRIBUTE, true);
        return INDEX_VIEW;
    }
    
    /**
     * Inner class to represent factorial calculation result
     */
    private static class FactorialResult {
        private final long value;
        private final double responseTime;
        
        public FactorialResult(long value, double responseTime) {
            this.value = value;
            this.responseTime = responseTime;
        }
        
        public long getValue() {
            return value;
        }
        
        public double getResponseTime() {
            return responseTime;
        }
    }
} 