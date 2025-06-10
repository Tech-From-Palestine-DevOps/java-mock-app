package com.example.tokenvalidator.util;

/**
 * Utility class for validating factorial calculation inputs.
 */
public class FactorialValidator {
    
    private static final int MIN_FACTORIAL_INPUT = 0;
    private static final int MAX_FACTORIAL_INPUT = 20;
    
    /**
     * Validates the input for factorial calculation.
     * 
     * @param number The number to validate
     * @throws IllegalArgumentException if the input is invalid
     */
    public static void validate(int number) {
        if (number < MIN_FACTORIAL_INPUT) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        if (number > MAX_FACTORIAL_INPUT) {
            throw new IllegalArgumentException("Number too large for factorial calculation (max 20)");
        }
    }
    
    /**
     * Checks if a number is valid for factorial calculation without throwing an exception.
     * 
     * @param number The number to check
     * @return true if the number is valid, false otherwise
     */
    public static boolean isValid(int number) {
        return number >= MIN_FACTORIAL_INPUT && number <= MAX_FACTORIAL_INPUT;
    }
} 