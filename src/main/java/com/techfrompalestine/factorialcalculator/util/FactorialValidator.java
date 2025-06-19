package com.techfrompalestine.factorialcalculator.util;

/**
 * Utility class for validating factorial calculation inputs.
 */
public class FactorialValidator {
    
    private static final int MAX_SAFE_FACTORIAL = 20;
    
    /**
     * Validates input for factorial calculation.
     * 
     * @param number The number to validate
     * @throws IllegalArgumentException if the input is invalid
     */
    public static void validateInput(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        if (number > MAX_SAFE_FACTORIAL) {
            throw new IllegalArgumentException(
                String.format("Input too large. Maximum safe input is %d", MAX_SAFE_FACTORIAL)
            );
        }
    }
} 