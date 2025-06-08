package com.example.tokenvalidator.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service responsible for mathematical calculations.
 * Currently supports factorial calculations with proper validation and error handling.
 */
@Service
public class CalculationService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);
    
    // Constants for factorial calculation limits
    private static final int MIN_FACTORIAL_INPUT = 0;
    private static final int MAX_SAFE_FACTORIAL_INPUT = 20; // 21! exceeds long capacity
    private static final long FACTORIAL_BASE_CASE = 1L;
    
    // Error messages
    private static final String NEGATIVE_NUMBER_ERROR = "Factorial is not defined for negative numbers";
    private static final String NUMBER_TOO_LARGE_ERROR = "Number too large for factorial calculation (max %d)";
    
    /**
     * Calculates the factorial of a given number using recursive approach.
     * 
     * @param number The number to calculate factorial for (must be 0-20)
     * @return The factorial result
     * @throws IllegalArgumentException if number is negative or greater than 20
     */
    public long factorial(int number) {
        validateFactorialInput(number);
        
        logger.debug("Calculating factorial for number: {}", number);
        
        long result = calculateFactorialRecursively(number);
        
        logger.debug("Factorial calculation completed: {}! = {}", number, result);
        
        return result;
    }
    
    /**
     * Alternative iterative factorial calculation method.
     * More memory efficient for larger numbers within the safe range.
     * 
     * @param number The number to calculate factorial for
     * @return The factorial result
     */
    public long factorialIterative(int number) {
        validateFactorialInput(number);
        
        if (number <= 1) {
            return FACTORIAL_BASE_CASE;
        }
        
        long result = FACTORIAL_BASE_CASE;
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        
        return result;
    }
    
    /**
     * Validates input for factorial calculation.
     * 
     * @param number The number to validate
     * @throws IllegalArgumentException if number is invalid
     */
    private void validateFactorialInput(int number) {
        if (number < MIN_FACTORIAL_INPUT) {
            throw new IllegalArgumentException(NEGATIVE_NUMBER_ERROR);
        }
        
        if (number > MAX_SAFE_FACTORIAL_INPUT) {
            throw new IllegalArgumentException(
                String.format(NUMBER_TOO_LARGE_ERROR, MAX_SAFE_FACTORIAL_INPUT)
            );
        }
    }
    
    /**
     * Recursive implementation of factorial calculation.
     * 
     * @param number The number to calculate factorial for
     * @return The factorial result
     */
    private long calculateFactorialRecursively(int number) {
        if (number <= 1) {
            return FACTORIAL_BASE_CASE;
        }
        
        return number * calculateFactorialRecursively(number - 1);
    }
    
    /**
     * Gets the maximum safe input for factorial calculation.
     * 
     * @return The maximum safe input value
     */
    public int getMaxSafeFactorialInput() {
        return MAX_SAFE_FACTORIAL_INPUT;
    }
    
    /**
     * Checks if a number is valid for factorial calculation.
     * 
     * @param number The number to check
     * @return true if the number is valid, false otherwise
     */
    public boolean isValidFactorialInput(int number) {
        return number >= MIN_FACTORIAL_INPUT && number <= MAX_SAFE_FACTORIAL_INPUT;
    }
} 