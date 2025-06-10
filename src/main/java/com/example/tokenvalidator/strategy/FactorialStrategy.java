package com.example.tokenvalidator.strategy;

/**
 * Interface for factorial calculation strategies.
 */
public interface FactorialStrategy {
    
    /**
     * Calculates the factorial of a number.
     * 
     * @param number The number to calculate factorial for
     * @return The factorial result
     * @throws InterruptedException if the calculation is interrupted
     */
    long calculate(int number) throws InterruptedException;
} 